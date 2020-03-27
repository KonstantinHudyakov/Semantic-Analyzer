package me.khudyakov.semanticanalyzer.components.semantictree;

import me.khudyakov.semanticanalyzer.program.Expression;

public abstract class StatementNode extends TreeNode {

    protected Expression expression;

    public StatementNode(Expression expression) {
        this.expression = expression;
    }

    public StatementNode(Expression expression, int startInd, int endInd) {
        super(startInd, endInd);
        this.expression = expression;
    }

    public Expression getExpression() {
        return expression;
    }

    public void setExpression(Expression expression) {
        this.expression = expression;
    }
}
