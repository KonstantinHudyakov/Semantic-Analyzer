package me.khudyakov.staticanalyzer.components.syntaxtree;

import me.khudyakov.staticanalyzer.components.atoms.Constant;
import me.khudyakov.staticanalyzer.editor.OutputAreaWriter;
import me.khudyakov.staticanalyzer.program.Expression;
import me.khudyakov.staticanalyzer.util.ExpressionExecutionException;

import java.util.Objects;

public class ExpressionNode extends StatementNode {

    public ExpressionNode(Expression expression) {
        super(expression);
    }

    public ExpressionNode(Expression expression, int startInd, int endInd) {
        super(expression, startInd, endInd);
    }

    @Override
    public void executeSubtree() throws ExpressionExecutionException {
        Constant res = expression.execute();
        OutputAreaWriter.println(res.toString());
    }

    @Override
    public boolean contentEquals(TreeNode node) {
        if(this == node) return true;
        if(node == null || getClass() != node.getClass()) return false;
        ExpressionNode expressionNode = (ExpressionNode) node;
        return Objects.equals(expression, expressionNode.getExpression());
    }


}
