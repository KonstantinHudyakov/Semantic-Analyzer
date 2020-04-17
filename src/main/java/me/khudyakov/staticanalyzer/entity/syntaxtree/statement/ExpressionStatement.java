package me.khudyakov.staticanalyzer.entity.syntaxtree.statement;

import me.khudyakov.staticanalyzer.editor.OutputAreaWriter;
import me.khudyakov.staticanalyzer.entity.syntaxtree.expression.Expression;

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

    public Expression getExpression() {
        return expression;
    }
}
