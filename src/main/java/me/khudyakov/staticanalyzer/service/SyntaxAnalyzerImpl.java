package me.khudyakov.staticanalyzer.service;

import me.khudyakov.staticanalyzer.entity.Token;
import me.khudyakov.staticanalyzer.entity.TokenType;
import me.khudyakov.staticanalyzer.entity.syntaxtree.SyntaxTree;
import me.khudyakov.staticanalyzer.entity.syntaxtree.expression.BinaryOperation;
import me.khudyakov.staticanalyzer.entity.syntaxtree.expression.Constant;
import me.khudyakov.staticanalyzer.entity.syntaxtree.expression.Expression;
import me.khudyakov.staticanalyzer.entity.syntaxtree.expression.Variable;
import me.khudyakov.staticanalyzer.entity.syntaxtree.expression.operator.*;
import me.khudyakov.staticanalyzer.entity.syntaxtree.statement.*;
import me.khudyakov.staticanalyzer.program.ProgramCode;
import me.khudyakov.staticanalyzer.util.SyntaxAnalyzerException;
import me.khudyakov.staticanalyzer.util.TokenUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static me.khudyakov.staticanalyzer.entity.TokenType.*;
import static me.khudyakov.staticanalyzer.util.TokenUtils.isExprStart;
import static me.khudyakov.staticanalyzer.util.TokenUtils.isTokenOfType;

public class SyntaxAnalyzerImpl implements SyntaxAnalyzer {

    @Override
    public SyntaxTree createSyntaxTree(ProgramCode programCode) throws SyntaxAnalyzerException {
        SyntaxTreeBuilder builder = new SyntaxTreeBuilder(programCode);
        return builder.buildTree();
    }

    private static class SyntaxTreeBuilder {

        private static final String ERROR = "Unexpected token \"%s\", ind = %d";
        private static final String PREMATURE_END = "Error! Premature end of program";

        private final Map<String, Variable> varNameToVariable = new HashMap<>();

        private final ProgramCode code;
        private int curInd = 0;

        SyntaxTreeBuilder(ProgramCode code) {
            this.code = code;
        }

        SyntaxTree buildTree() throws SyntaxAnalyzerException {
            int startInd = curInd;
            List<Statement> statementList = statementList();
            BlockStatement blockStatement = new BlockStatement(statementList, startInd, curInd - 1);
            return new SyntaxTree(blockStatement);
        }

        private List<Statement> statementList() throws SyntaxAnalyzerException {
            List<Statement> statements = new ArrayList<>();
            while (curInd < code.size()) {
                statements.add(statement());
            }
            return statements;
        }

        private Statement statement() throws SyntaxAnalyzerException {
            Token token = getCurOrThrow();
            Statement statement = null;
            if (isTokenOfType(token, IF)) {
                statement = ifStatement();
            } else if (isTokenOfType(token, ASSIGN_IDENTIFIER)) {
                statement = assignStatement();
            } else if (isTokenOfType(token, OPEN_BRACE)) {
                statement = blockStatement();
            } else if (isExprStart(token)) {
                statement = expressionStatement();
            } else {
                throwError(token);
            }
            return statement;
        }

        private BlockStatement blockStatement() throws SyntaxAnalyzerException {
            int startInd = curInd;
            checkTypeOfCurOrThrow(OPEN_BRACE);
            List<Statement> statementList = new ArrayList<>();
            while (checkTypeOfCur(TokenUtils::isStatementStart)) {
                statementList.add(statement());
            }
            checkTypeOfCurOrThrow(CLOSE_BRACE);

            return new BlockStatement(statementList, startInd, curInd - 1);
        }

        private IfStatement ifStatement() throws SyntaxAnalyzerException {
            int startInd = curInd;
            checkTypeOfCurOrThrow(IF);
            checkTypeOfCurOrThrow(OPEN_PARENTHESIS);
            Expression condition = expression();
            checkTypeOfCurOrThrow(CLOSE_PARENTHESIS);
            int endInd = curInd - 1;
            Statement body = statement();

            return new IfStatement(condition, body, startInd, endInd);
        }

        private AssignStatement assignStatement() throws SyntaxAnalyzerException {
            int startInd = curInd;
            checkTypeOfCurOrThrow(ASSIGN_IDENTIFIER);
            Variable variable = variable();
            checkTypeOfCurOrThrow(ASSIGN);
            Expression expr = expression();
            checkTypeOfCurOrThrow(SEMICOLON);

            return new AssignStatement(variable, expr, startInd, curInd - 1);
        }

        private ExpressionStatement expressionStatement() throws SyntaxAnalyzerException {
            int startInd = curInd;
            Expression expr = expression();
            checkTypeOfCurOrThrow(SEMICOLON);

            return new ExpressionStatement(expr, startInd, curInd - 1);
        }

        /**
         * Заданная грамматика для арифметических выражений:
         * Expression -> PlusMinusExpr | PlusMinusExpr > PlusMinusExpr | PlusMinusExpr < PlusMinusExpr
         * PlusMinusExpr -> MultDivExpr | PlusMinusExpr + MultDivExpr | PlusMinusExpr - MultDivExpr
         * MultDivExpr -> SimpleExpr | MultDivExpr * SimpleExpr | MultDivExpr / SimpleExpr
         * SimpleExpression -> Identifier | Integer | ( Expression )
         *
         * Избавимся от левой рекурсии:
         *
         * Expression -> PlusMinusExpr | PlusMinusExpr > PlusMinusExpr | PlusMinusExpr < PlusMinusExpr
         * PlusMinusExpr -> MultDivExpr | MultDivExpr + PlusMinusExpr2 | MultDivExpr - PlusMinusExpr2
         * PlusMinusExpr2 -> MultDivExpr | MultDivExpr + PlusMinusExpr2 | MultDivExpr - PlusMinusExpr2
         * MultDivExpr -> SimpleExpr | SimpleExpr * MultDivExpr2 | SimpleExpr / MultDivExpr2
         * MultDivExpr2 -> SimpleExpr | SimpleExpr * MultDivExpr2 | SimpleExpr / MultDivExpr2
         * SimpleExpression -> Identifier | Integer | ( Expression )
         *
         * Заменим левую рекурсию на цикл
         */
        private Expression expression() throws SyntaxAnalyzerException {
            Expression leftExpr = plusMinusExpression();
            if (checkTypeOfCur(TokenUtils::isCompareOperation)) {
                Token cur = getCurOrThrow();
                BinaryOperator operator = isTokenOfType(cur, GREATER) ? new GreaterOperator() : new LessOperator();
                curInd++;
                Expression rightExpr = plusMinusExpression();
                return new BinaryOperation(operator, leftExpr, rightExpr);
            }
            return leftExpr;
        }

        /**
         * PlusMinusExpr -> MultDivExpr | MultDivExpr + PlusMinusExpr2 | MultDivExpr - PlusMinusExpr2
         * PlusMinusExpr2 -> MultDivExpr | MultDivExpr + PlusMinusExpr2 | MultDivExpr - PlusMinusExpr2
         */
        private Expression plusMinusExpression() throws SyntaxAnalyzerException {
            Expression expr = multiplyDivisionExpression();
            while (checkTypeOfCur(TokenUtils::isPlusMinus)) {
                Token cur = getCurOrThrow();
                BinaryOperator operator = isTokenOfType(cur, ADDITION) ? new AdditionOperator() : new SubtractionOperator();
                curInd++;
                Expression rightExpr = multiplyDivisionExpression();
                expr = new BinaryOperation(operator, expr, rightExpr);
            }
            return expr;
        }

        /**
         * MultDivExpr -> SimpleExpr | SimpleExpr * MultDivExpr2 | SimpleExpr / MultDivExpr2
         * MultDivExpr2 -> SimpleExpr | SimpleExpr * MultDivExpr2 | SimpleExpr / MultDivExpr2
         */
        private Expression multiplyDivisionExpression() throws SyntaxAnalyzerException {
            Expression expr = simpleExpression();
            while (checkTypeOfCur(TokenUtils::isMultiplyDivision)) {
                Token cur = getCurOrThrow();
                BinaryOperator operator = isTokenOfType(cur, DIVISION) ? new DivisionOperator() : new MultiplicationOperator();
                curInd++;
                Expression rightExpr = simpleExpression();
                expr = new BinaryOperation(operator, expr, rightExpr);
            }
            return expr;
        }

        /**
         * SimpleExpression -> Identifier | Integer | ( Expression )
         */
        private Expression simpleExpression() throws SyntaxAnalyzerException {
            Token cur = getCurOrThrow();
            Expression expr = null;
            if (isTokenOfType(cur, OPEN_PARENTHESIS)) {
                curInd++;
                expr = expression();
                checkTypeOfCurOrThrow(CLOSE_PARENTHESIS);
            } else if (isTokenOfType(cur, INTEGER)) {
                int value = Integer.parseInt(cur.getValue());
                expr = new Constant(value);
                curInd++;
            } else if (isTokenOfType(cur, IDENTIFIER)) {
                expr = variable();
            } else {
                throwError(cur);
            }

            return expr;
        }

        private Variable variable() throws SyntaxAnalyzerException {
            Token cur = getCurOrThrow();
            if (!isTokenOfType(cur, IDENTIFIER)) {
                throwError(cur);
            }
            String varName = cur.getValue();
            Variable variable;
            if (varNameToVariable.containsKey(varName)) {
                variable = varNameToVariable.get(varName);
            } else {
                variable = new Variable(varName);
                varNameToVariable.put(varName, variable);
            }
            curInd++;
            return variable;
        }


        private boolean checkTypeOfCur(TokenType type) throws SyntaxAnalyzerException {
            return checkTypeOfCur(token -> isTokenOfType(token, type));
        }

        private boolean checkTypeOfCur(Function<Token, Boolean> condition) throws SyntaxAnalyzerException {
            Token cur = getCurOrThrow();
            return condition.apply(cur);
        }

        private void checkTypeOfCurOrThrow(TokenType type) throws SyntaxAnalyzerException {
            checkTypeOfCurOrThrow(token -> isTokenOfType(token, type));
        }

        private void checkTypeOfCurOrThrow(Function<Token, Boolean> condition) throws SyntaxAnalyzerException {
            Token cur = getCurOrThrow();
            throwIfNotTypeOf(cur, condition);
            curInd++;
        }

        private void throwIfNotTypeOf(Token token, Function<Token, Boolean> condition) throws SyntaxAnalyzerException {
            if (!condition.apply(token)) {
                throwError(token);
            }
        }

        private void throwIfNotTypeOf(Token token, TokenType type) throws SyntaxAnalyzerException {
            if (!isTokenOfType(token, type)) {
                throwError(token);
            }
        }

        private Token getCurOrThrow() throws SyntaxAnalyzerException {
            if (curInd >= code.size()) {
                throw new SyntaxAnalyzerException(PREMATURE_END);
            }
            return code.get(curInd);
        }

        private void throwError(Token token) throws SyntaxAnalyzerException {
            throw new SyntaxAnalyzerException(String.format(ERROR, token.getValue(), curInd));
        }
    }
}
