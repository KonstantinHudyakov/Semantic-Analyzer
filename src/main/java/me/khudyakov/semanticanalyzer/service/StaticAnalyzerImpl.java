package me.khudyakov.semanticanalyzer.service;

import me.khudyakov.semanticanalyzer.components.*;
import me.khudyakov.semanticanalyzer.components.atoms.Atom;
import me.khudyakov.semanticanalyzer.components.atoms.Variable;
import me.khudyakov.semanticanalyzer.components.brackets.CloseBrace;
import me.khudyakov.semanticanalyzer.components.brackets.CloseParenthesis;
import me.khudyakov.semanticanalyzer.components.brackets.OpenBrace;
import me.khudyakov.semanticanalyzer.components.brackets.OpenParenthesis;
import me.khudyakov.semanticanalyzer.components.operations.Operation;
import me.khudyakov.semanticanalyzer.components.semantictree.*;
import me.khudyakov.semanticanalyzer.program.Expression;
import me.khudyakov.semanticanalyzer.program.ProgramCode;
import me.khudyakov.semanticanalyzer.program.SemanticTree;
import me.khudyakov.semanticanalyzer.util.ExpressionConverterException;
import me.khudyakov.semanticanalyzer.util.StaticAnalyzerException;

public class StaticAnalyzerImpl implements StaticAnalyzer {

    private final ExpressionConverterImpl expressionConverter = new ExpressionConverterImpl();

    public SemanticTree analyze(ProgramCode programCode) throws StaticAnalyzerException, ExpressionConverterException {
        StatementListNode statementListNode = statementList(programCode, 0);
        if(statementListNode.getEndInd() + 1 != programCode.size()) {
            throw new StaticAnalyzerException("Преждевременный конец программы, ind = " + statementListNode.getEndInd());
        }
        return new SemanticTree(statementListNode);
    }

    private StatementListNode statementList(ProgramCode programCode, int ind) throws StaticAnalyzerException, ExpressionConverterException {
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

    private TreeNode statement(ProgramCode programCode, int ind) throws StaticAnalyzerException, ExpressionConverterException {
        Lexeme cur = programCode.get(ind);
        TreeNode node = null;
        if (cur instanceof IfStatement) {
            node = ifStatement(programCode, ind);
        } else if (cur instanceof AssignIdentifier) {
            node = assignStatement(programCode, ind);
        } else if (cur instanceof OpenBrace) {
            node = blockStatement(programCode, ind);
        } else if (cur instanceof Atom || cur instanceof Operation || cur instanceof OpenParenthesis) {
            node = expressionStatement(programCode, ind);
        }
        return node;
    }

    private ConditionNode ifStatement(ProgramCode programCode, int ind) throws StaticAnalyzerException, ExpressionConverterException {
        int n = programCode.size();
        int beginInd = ind;
        if (ind + 1 < n && programCode.get(ind + 1) instanceof OpenParenthesis) {
            ind++;
            if (ind + 1 < n && (programCode.get(ind + 1) instanceof Atom || programCode.get(ind + 1) instanceof Operation
                    || programCode.get(ind + 1) instanceof OpenParenthesis)) {
                ind++;
                int beginExpr = ind;
                int openParenthesisNum = 0;
                while (ind < n && openParenthesisNum >= 0) {
                    Lexeme cur = programCode.get(ind);
                    if (cur instanceof OpenParenthesis) {
                        openParenthesisNum++;
                    } else if (cur instanceof CloseParenthesis) {
                        openParenthesisNum--;
                    }
                    ind++;
                }
                if (ind >= n) {
                    throw new StaticAnalyzerException("Ошибка при чтении ifStatement, ind = " + ind);
                }
                Expression expression = new Expression(programCode.subList(beginExpr, ind - 1));
                if (!(programCode.get(ind) instanceof CloseParenthesis
                        || programCode.get(ind) instanceof CloseBrace
                        || programCode.get(ind) instanceof Assign
                        || programCode.get(ind) instanceof Semicolon)) {
                    TreeNode statement = statement(programCode, ind);
                    expressionConverter.convertToPostfix(expression);
                    return new ConditionNode(expression, statement, beginInd, statement.getEndInd());
                }
            }
        }
        throw new StaticAnalyzerException("Ошибка при чтении ifStatement, ind = " + ind);
    }

    private AssignNode assignStatement(ProgramCode programCode, int ind) throws StaticAnalyzerException, ExpressionConverterException {
        int n = programCode.size();
        int beginInd = ind;
        // Проверка на соответствие грамматике: Variable = Expression
        if (ind + 3 >= n || !(programCode.get(ind + 1) instanceof Variable) || !(programCode.get(ind + 2) instanceof Assign)
                || !(programCode.get(ind + 3) instanceof Atom || programCode.get(ind + 3) instanceof Operation
                || programCode.get(ind + 3) instanceof OpenParenthesis)) {
            throw new StaticAnalyzerException("Ошибка при чтении assignStatement, ind = " + ind);
        }
        Variable var = (Variable) programCode.get(ind + 1);
        ind += 3;
        int beginExpr = ind;
        while (ind < n && !(programCode.get(ind) instanceof Semicolon)) {
            ind++;
        }
        if (ind >= n && !(programCode.get(ind) instanceof Semicolon)) {
            throw new StaticAnalyzerException("Ошибка при чтении assignStatement, ind = " + ind);
        }
        Expression expression = new Expression(programCode.subList(beginExpr, ind));
        expressionConverter.convertToPostfix(expression);
        return new AssignNode(expression, var, beginInd, ind);
    }

    private BlockStatementsNode blockStatement(ProgramCode programCode, int ind) throws StaticAnalyzerException, ExpressionConverterException {
        int n = programCode.size();
        BlockStatementsNode nodeList = new BlockStatementsNode();
        nodeList.setStartInd(ind);
        ind++;
        while (ind < n && !(programCode.get(ind) instanceof CloseBrace)) {
            TreeNode node = statement(programCode, ind);
            nodeList.add(node);
            ind = node.getEndInd() + 1;
        }
        if(ind >= n) {
            throw new StaticAnalyzerException("Преждевременный конец программы, ind = " + ind);
        }
        nodeList.setEndInd(ind);
        return nodeList;
//        nodeList.setEndInd(ind);
//        if(ind < n && !(program.get(ind) instanceof CloseParenthesis
//                || program.get(ind) instanceof CloseBrace
//                || program.get(ind) instanceof Assign
//                || program.get(ind) instanceof Semicolon)) {
//            StatementListNode statementListNode = statementList(program, ind);
//            ind = statementListNode.getEndInd();
//            if(ind + 1 < n && program.get(ind + 1) instanceof CloseBrace) {
//                return new BlockStatementsNode(statementListNode.getStatements(), beginInd, ind + 1);
//            }
//        }
//        throw new StaticAnalyzerException("Ошибка при чтении blockStatement, ind = " + ind);
    }

    private ExpressionNode expressionStatement(ProgramCode programCode, int ind) throws ExpressionConverterException {
        int beginInd = ind;
        while (ind < programCode.size() && !(programCode.get(ind) instanceof Semicolon)) {
            ind++;
        }

        Expression expression = new Expression(programCode.subList(beginInd, ind));
        expressionConverter.convertToPostfix(expression);
        return new ExpressionNode(expression, beginInd, ind);
    }

}
