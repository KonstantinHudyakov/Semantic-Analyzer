package me.khudyakov.staticanalyzer.service;

import me.khudyakov.staticanalyzer.components.*;
import me.khudyakov.staticanalyzer.components.atoms.Atom;
import me.khudyakov.staticanalyzer.components.atoms.Variable;
import me.khudyakov.staticanalyzer.components.brackets.CloseBrace;
import me.khudyakov.staticanalyzer.components.brackets.CloseParenthesis;
import me.khudyakov.staticanalyzer.components.brackets.OpenBrace;
import me.khudyakov.staticanalyzer.components.brackets.OpenParenthesis;
import me.khudyakov.staticanalyzer.components.operations.Operation;
import me.khudyakov.staticanalyzer.components.syntaxtree.*;
import me.khudyakov.staticanalyzer.program.Expression;
import me.khudyakov.staticanalyzer.program.ProgramCode;
import me.khudyakov.staticanalyzer.program.SyntaxTree;
import me.khudyakov.staticanalyzer.util.ExpressionConverterException;
import me.khudyakov.staticanalyzer.util.ExpressionExecutionException;
import me.khudyakov.staticanalyzer.util.SyntaxAnalyzerException;

import java.util.Collections;

public class SyntaxAnalyzerImpl implements SyntaxAnalyzer {

    private final ExpressionConverterImpl expressionConverter = new ExpressionConverterImpl();

    public SyntaxTree analyze(ProgramCode programCode) throws SyntaxAnalyzerException, ExpressionConverterException, ExpressionExecutionException {
        StatementListNode statementListNode = statementList(programCode, 0);
        if (statementListNode.getEndInd() + 1 != programCode.size()) {
            throw new SyntaxAnalyzerException("Преждевременный конец программы, ind = " + statementListNode.getEndInd());
        }
        return new SyntaxTree(statementListNode);
    }

    private StatementListNode statementList(ProgramCode programCode,
                                            int ind) throws SyntaxAnalyzerException, ExpressionConverterException, ExpressionExecutionException {
        StatementListNode nodeList = new StatementListNode();
        nodeList.setStartInd(ind);
        while (ind < programCode.size()) {
            TreeNode node = statement(programCode, ind);
            nodeList.add(node);
            ind = node.getEndInd() + 1;
        }
        nodeList.setEndInd(ind - 1);
        return nodeList;
    }

    private TreeNode statement(ProgramCode programCode,
                               int ind) throws SyntaxAnalyzerException, ExpressionConverterException, ExpressionExecutionException {
        Token cur = programCode.get(ind);
        TreeNode node = null;
        if (cur instanceof IfStatement) {
            node = ifStatement(programCode, ind);
        } else if (cur instanceof AssignIdentifier) {
            node = assignStatement(programCode, ind);
        } else if (cur instanceof OpenBrace) {
            node = blockStatement(programCode, ind);
        } else if (cur instanceof Atom || cur instanceof Operation || cur instanceof OpenParenthesis) {
            node = expressionStatement(programCode, ind);
        } else {
            throw new SyntaxAnalyzerException("Ошибка при чтении statement, ind = " + ind);
        }
        return node;
    }

    private ConditionNode ifStatement(ProgramCode programCode,
                                      int ind) throws SyntaxAnalyzerException, ExpressionConverterException, ExpressionExecutionException {
        int n = programCode.size();
        int beginInd = ind;
        if (ind + 1 < n && programCode.get(ind + 1) instanceof OpenParenthesis) {
            ind++;
            Expression expression = null;
            if (ind + 1 < n && (programCode.get(ind + 1) instanceof Atom || programCode.get(ind + 1) instanceof Operation
                    || programCode.get(ind + 1) instanceof OpenParenthesis)) {
                ind++;
                int beginExpr = ind;
                int openParenthesisNum = 0;
                while (ind < n && openParenthesisNum >= 0) {
                    Token cur = programCode.get(ind);
                    if (cur instanceof OpenParenthesis) {
                        openParenthesisNum++;
                    } else if (cur instanceof CloseParenthesis) {
                        openParenthesisNum--;
                    }
                    ind++;
                }
                if (ind >= n) {
                    throw new SyntaxAnalyzerException("Ошибка при чтении ifStatement, ind = " + ind);
                }
                expression = new Expression(programCode.subList(beginExpr, ind - 1));
                expressionConverter.convertToPostfix(expression);
            } else if (ind + 1 < n && programCode.get(ind + 1) instanceof CloseParenthesis) {
                expression = new Expression(Collections.emptyList());
                ind += 2;
            } else {
                throw new SyntaxAnalyzerException("Ошибка при чтении ifStatement, ind = " + ind);
            }
            if (!(programCode.get(ind) instanceof CloseParenthesis
                    || programCode.get(ind) instanceof CloseBrace
                    || programCode.get(ind) instanceof Assign
                    || programCode.get(ind) instanceof Semicolon)) {
                //int endInd = ind - 1;
                TreeNode statement = statement(programCode, ind);
                return new ConditionNode(expression, statement, beginInd, statement.getEndInd());
            }
        }
        throw new SyntaxAnalyzerException("Ошибка при чтении ifStatement, ind = " + ind);
    }

    private AssignNode assignStatement(ProgramCode programCode,
                                       int ind) throws SyntaxAnalyzerException, ExpressionConverterException, ExpressionExecutionException {
        int n = programCode.size();
        int beginInd = ind;
        // Проверка на соответствие грамматике: Variable = Expression
        if (ind + 3 >= n || !(programCode.get(ind + 1) instanceof Variable) || !(programCode.get(ind + 2) instanceof Assign)
                || !(programCode.get(ind + 3) instanceof Atom || programCode.get(ind + 3) instanceof Operation
                || programCode.get(ind + 3) instanceof OpenParenthesis)) {
            throw new SyntaxAnalyzerException("Ошибка при чтении assignStatement, ind = " + ind);
        }
        Variable var = (Variable) programCode.get(ind + 1);
        ind += 3;
        int beginExpr = ind;
        while (ind < n && !(programCode.get(ind) instanceof Semicolon)) {
            ind++;
        }
        if (ind >= n || !(programCode.get(ind) instanceof Semicolon)) {
            throw new SyntaxAnalyzerException("Ошибка при чтении assignStatement, ind = " + ind);
        }
        Expression expression = new Expression(programCode.subList(beginExpr, ind));
        expressionConverter.convertToPostfix(expression);
        return new AssignNode(expression, var, beginInd, ind);
    }

    private BlockStatementsNode blockStatement(ProgramCode programCode,
                                               int ind) throws SyntaxAnalyzerException, ExpressionConverterException, ExpressionExecutionException {
        int n = programCode.size();
        BlockStatementsNode nodeList = new BlockStatementsNode();
        nodeList.setStartInd(ind);
        ind++;
        while (ind < n && !(programCode.get(ind) instanceof CloseBrace)) {
            TreeNode node = statement(programCode, ind);
            nodeList.add(node);
            ind = node.getEndInd() + 1;
        }
        if (ind >= n) {
            throw new SyntaxAnalyzerException("Преждевременный конец программы, ind = " + ind);
        }
        nodeList.setEndInd(ind);
        return nodeList;
    }

    private ExpressionNode expressionStatement(ProgramCode programCode,
                                               int ind) throws ExpressionConverterException, ExpressionExecutionException {
        int beginInd = ind;
        while (ind < programCode.size() && !(programCode.get(ind) instanceof Semicolon)) {
            ind++;
        }

        Expression expression = new Expression(programCode.subList(beginInd, ind));
        expressionConverter.convertToPostfix(expression);
        return new ExpressionNode(expression, beginInd, ind);
    }

}
