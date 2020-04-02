package me.khudyakov.staticanalyzer.components.syntaxtree;

import me.khudyakov.staticanalyzer.program.Expression;
import me.khudyakov.staticanalyzer.components.atoms.Constant;
import me.khudyakov.staticanalyzer.components.atoms.Variable;
import me.khudyakov.staticanalyzer.util.ExpressionExecutionException;

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

    public Variable getVariable() {
        return variable;
    }

    public void setVariable(Variable variable) {
        this.variable = variable;
    }
}
