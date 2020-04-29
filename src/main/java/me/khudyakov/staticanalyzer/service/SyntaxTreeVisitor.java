package me.khudyakov.staticanalyzer.service;

import me.khudyakov.staticanalyzer.entity.syntaxtree.statement.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public interface SyntaxTreeVisitor {

    default void visit(Statement statement) {
        Class<? extends Statement> statementClass = statement.getClass();
        if (statementClass == AssignStatement.class) {
            visit((AssignStatement) statement);
        } else if (statementClass == ExpressionStatement.class) {
            visit((ExpressionStatement) statement);
        } else if (statementClass == IfStatement.class) {
            visit((IfStatement) statement);
        } else if (statementClass == BlockStatement.class) {
            visit((BlockStatement) statement);
        } else {
            throw new NotImplementedException();
        }
    }

    void visit(AssignStatement assignStatement);

    void visit(ExpressionStatement expressionStatement);

    void visit(IfStatement ifStatement);

    void visit(BlockStatement blockStatement);
}
