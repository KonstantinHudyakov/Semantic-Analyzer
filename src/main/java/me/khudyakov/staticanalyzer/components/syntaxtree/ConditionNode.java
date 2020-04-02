package me.khudyakov.staticanalyzer.components.syntaxtree;

import me.khudyakov.staticanalyzer.program.Expression;
import me.khudyakov.staticanalyzer.util.ExpressionExecutionException;

public class ConditionNode extends StatementNode {

    private TreeNode body;

    public ConditionNode(Expression expression, TreeNode body) {
        super(expression);
        this.body = body;
    }

    public ConditionNode(Expression expression, TreeNode body, int startInd, int endInd) {
        super(expression, startInd, endInd);
        this.body = body;
    }

    @Override
    public void executeSubtree() throws ExpressionExecutionException {
        if(checkCondition()) {
            body.executeSubtree();
        }
    }

    private boolean checkCondition() throws ExpressionExecutionException {
        return expression.execute().getValue() != 0;
    }

    public TreeNode getBody() {
        return body;
    }

    public void setBody(TreeNode body) {
        this.body = body;
    }
}
