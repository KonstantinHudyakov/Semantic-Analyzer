package me.khudyakov.staticanalyzer.entity.syntaxtree.statement;

import me.khudyakov.staticanalyzer.entity.syntaxtree.expression.Expression;

public class IfStatement extends Statement {

    private final Expression condition;
    private final Statement body;

    public IfStatement(Expression condition, Statement body, int startInd, int endInd) {
        super(startInd, endInd);
        this.condition = condition;
        this.body = body;
    }

    @Override
    public void execute() {
        if(condition.execute() != 0) {
            body.execute();
        }
    }

    public Expression getCondition() {
        return condition;
    }

    public Statement getBody() {
        return body;
    }
}
