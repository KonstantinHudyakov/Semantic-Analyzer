package me.khudyakov.staticanalyzer.entity.syntaxtree.statement;

import java.util.Collections;
import java.util.List;

public class CommentStatement extends Statement {

    private final String value;

    public CommentStatement(String value, int startInd, int endInd) {
        super(startInd, endInd);
        this.value = value;
    }

    @Override
    public void execute() {
        // do nothing
    }

    @Override
    public List<? extends Statement> getChildren() {
        return Collections.emptyList();
    }

    @Override
    public boolean contentEquals(Statement statement) {
        return super.contentEquals(statement)
                && value.equals(((CommentStatement) statement).value);
    }

    public String getValue() {
        return value;
    }
}
