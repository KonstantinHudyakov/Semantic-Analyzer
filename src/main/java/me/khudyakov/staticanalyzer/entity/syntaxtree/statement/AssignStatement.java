package me.khudyakov.staticanalyzer.entity.syntaxtree.statement;

import me.khudyakov.staticanalyzer.entity.syntaxtree.expression.Expression;
import me.khudyakov.staticanalyzer.entity.syntaxtree.expression.Variable;

public class AssignStatement extends Statement {

    private final Variable variable;
    private final Expression expr;

    public AssignStatement(Variable variable, Expression expr, int startInd, int endInd) {
        super(startInd, endInd);
        this.variable = variable;
        this.expr = expr;
    }

    @Override
    public void execute() {
        int value = expr.execute();
        variable.setValue(value);
    }

    public Variable getVariable() {
        return variable;
    }

    public Expression getExpr() {
        return expr;
    }
}
