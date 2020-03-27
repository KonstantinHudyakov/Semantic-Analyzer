package me.khudyakov.semanticanalyzer.components.semantictree;

import me.khudyakov.semanticanalyzer.program.Expression;

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
    public void execute() {
        if(checkCondition()) {
            body.execute();
        }
    }

    private boolean checkCondition() {
        return expression.execute().getValue() != 0;
    }

    public TreeNode getBody() {
        return body;
    }

    public void setBody(TreeNode body) {
        this.body = body;
    }
}
