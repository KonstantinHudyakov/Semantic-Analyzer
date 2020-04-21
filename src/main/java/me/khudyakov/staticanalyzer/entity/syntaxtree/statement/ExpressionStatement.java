package me.khudyakov.staticanalyzer.entity.syntaxtree.statement;

import me.khudyakov.staticanalyzer.editor.OutputAreaWriter;
import me.khudyakov.staticanalyzer.entity.syntaxtree.expression.Expression;

import java.util.Collections;
import java.util.List;

public class ExpressionStatement extends Statement {

    private final Expression expression;

    public ExpressionStatement(Expression expression, int startInd, int endInd) {
        super(startInd, endInd);
        this.expression = expression;
    }

    @Override
    public void execute() {
        int value = expression.execute();
        OutputAreaWriter.println(String.valueOf(value));
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


    // this method may be used in future
    public Expression getExpression() {
        return expression;
    }
}
