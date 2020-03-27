package me.khudyakov.semanticanalyzer.program;

import me.khudyakov.semanticanalyzer.components.semantictree.StatementListNode;

public class SemanticTree {

    private StatementListNode root;

    public SemanticTree(StatementListNode root) {
        this.root = root;
    }

    public StatementListNode getRoot() {
        return root;
    }

    public void setRoot(StatementListNode root) {
        this.root = root;
    }
}
