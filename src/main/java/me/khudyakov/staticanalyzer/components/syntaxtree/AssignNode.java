package me.khudyakov.staticanalyzer.components.syntaxtree;

import me.khudyakov.staticanalyzer.program.Expression;
import me.khudyakov.staticanalyzer.components.atoms.Constant;
import me.khudyakov.staticanalyzer.components.atoms.Variable;
import me.khudyakov.staticanalyzer.util.ExpressionExecutionException;

import java.util.Objects;

public class AssignNode extends StatementNode {

    private Variable variable;

    public AssignNode(Expression expression, Variable variable) {
        super(expression);
        this.variable = variable;
    }

    public AssignNode(Expression expression, Variable variable, int startInd, int endInd) {
        super(expression, startInd, endInd);
        this.variable = variable;
    }

    @Override
    public void executeSubtree() throws ExpressionExecutionException {
        Constant res = expression.execute();
        variable.setValue(res.getValue());
    }

    @Override
    public boolean contentEquals(TreeNode node) {
        if(this == node) return true;
        if(node == null || getClass() != node.getClass()) return false;
        AssignNode assignNode = (AssignNode) node;
        return Objects.equals(expression, assignNode.getExpression())
                && Objects.equals(variable, assignNode.getVariable());
    }


    public Variable getVariable() {
        return variable;
    }

    public void setVariable(Variable variable) {
        this.variable = variable;
    }
}
