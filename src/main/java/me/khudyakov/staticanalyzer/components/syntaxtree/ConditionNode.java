package me.khudyakov.staticanalyzer.components.syntaxtree;

import me.khudyakov.staticanalyzer.program.Expression;
import me.khudyakov.staticanalyzer.util.ExpressionExecutionException;

import java.util.Objects;

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

    @Override
    public boolean contentEquals(TreeNode node) {
        if(this == node) return true;
        if(node == null || getClass() != node.getClass()) return false;
        ConditionNode conditionNode = (ConditionNode) node;
        return Objects.equals(expression, conditionNode.getExpression());
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
