package me.khudyakov.staticanalyzer.program;

import me.khudyakov.staticanalyzer.components.syntaxtree.TreeNode;

public class SyntaxTreeChange {

    public static final SyntaxTreeChange NOTHING_CHANGED = new SyntaxTreeChange(TreeNode.EMPTY_NODE, ChangeType.ADD);

    private final TreeNode changedNode;
    private final ChangeType changeType;

    public SyntaxTreeChange(TreeNode changedNode, ChangeType changeType) {
        this.changedNode = changedNode;
        this.changeType = changeType;
    }

    public TreeNode getChangedNode() {
        return changedNode;
    }

    public ChangeType getChangeType() {
        return changeType;
    }


    public enum ChangeType {
        INSERT, ADD, REMOVE, EDIT;
    }
}
