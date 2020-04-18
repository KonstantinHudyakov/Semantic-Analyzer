package me.khudyakov.staticanalyzer.entity.syntaxtree.statement;

import me.khudyakov.staticanalyzer.entity.syntaxtree.expression.Expression;
import me.khudyakov.staticanalyzer.entity.syntaxtree.expression.Variable;

import java.util.Collections;
import java.util.List;

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

    @Override
    public List<? extends Statement> getChildren() {
        return Collections.emptyList();
    }

    @Override
    public boolean contentEquals(Statement statement) {
        if(!super.contentEquals(statement)) return false;
        AssignStatement assignStatement = (AssignStatement) statement;
        return variable.equals(assignStatement.variable)
                && expr.equals(assignStatement.expr);
    }

    public Variable getVariable() {
        return variable;
    }

    public Expression getExpr() {
        return expr;
    }
}
