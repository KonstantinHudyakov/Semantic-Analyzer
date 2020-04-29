package me.khudyakov.staticanalyzer.entity.syntaxtree.statement;

import me.khudyakov.staticanalyzer.entity.syntaxtree.expression.Expression;

import java.util.Collections;
import java.util.List;

public class ExpressionStatement extends Statement {

    private final Expression expression;

    public ExpressionStatement(Expression expression) {
        this.expression = expression;
    }

    @Override
    public List<? extends Statement> getChildren() {
        return Collections.emptyList();
    }

    @Override
    public boolean contentEquals(Statement statement) {
        return getClass() == statement.getClass()
                && expression.equals(((ExpressionStatement) statement).expression);
    }

    public Expression getExpression() {
        return expression;
    }
}
