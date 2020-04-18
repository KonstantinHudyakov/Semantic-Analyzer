package me.khudyakov.staticanalyzer.entity.syntaxtree;

import me.khudyakov.staticanalyzer.entity.syntaxtree.statement.Statement;

public class SyntaxTreeChange {

    public static final SyntaxTreeChange NOTHING_CHANGED = new SyntaxTreeChange(Statement.EMPTY_STATEMENT, ChangeType.NOTHING);

    private final Statement changedNode;
    private final ChangeType changeType;

    public SyntaxTreeChange(Statement changedNode, ChangeType changeType) {
        this.changedNode = changedNode;
        this.changeType = changeType;
    }

    public Statement getChangedNode() {
        return changedNode;
    }

    public ChangeType getChangeType() {
        return changeType;
    }


    public enum ChangeType {
        INSERT, REMOVE, EDIT, NOTHING;
    }
}
