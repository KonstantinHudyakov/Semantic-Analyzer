package me.khudyakov.staticanalyzer.components.syntaxtree;

import me.khudyakov.staticanalyzer.util.ExpressionExecutionException;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public abstract class TreeNode {

    public static final TreeNode EMPTY_NODE = new TreeNode() {
        @Override
        public void executeSubtree() throws ExpressionExecutionException {
            throw new NotImplementedException();
        }

        @Override
        public boolean contentEquals(TreeNode node) {
            return false;
        }
    };

    private int startInd;
    private int endInd;

    private TreeNode next;

    public TreeNode() {
    }

    public TreeNode(int startInd, int endInd) {
        this.startInd = startInd;
        this.endInd = endInd;
    }

    public abstract void executeSubtree() throws ExpressionExecutionException;

    public abstract boolean contentEquals(TreeNode node);

    public int getStartInd() {
        return startInd;
    }

    public void setStartInd(int startInd) {
        this.startInd = startInd;
    }

    public int getEndInd() {
        return endInd;
    }

    public void setEndInd(int endInd) {
        this.endInd = endInd;
    }

    public TreeNode getNext() {
        return next;
    }

    public void setNext(TreeNode next) {
        this.next = next;
    }
}
