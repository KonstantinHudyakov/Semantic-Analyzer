package me.khudyakov.staticanalyzer.entity.syntaxtree.statement;

import me.khudyakov.staticanalyzer.entity.syntaxtree.expression.Expression;

import java.util.Collections;
import java.util.List;

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
        if (condition.execute() != 0) {
            body.execute();
        }
    }

    @Override
    public List<? extends Statement> getChildren() {
        return Collections.singletonList(body);
    }

    @Override
    public boolean contentEquals(Statement statement) {
        return super.contentEquals(statement)
                && condition.equals(((IfStatement) statement).condition);
    }

    // this method may be used in future
    public Expression getCondition() {
        return condition;
    }

    public Statement getBody() {
        return body;
    }
}
