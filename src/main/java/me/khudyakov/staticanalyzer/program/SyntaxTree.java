package me.khudyakov.staticanalyzer.program;

import me.khudyakov.staticanalyzer.components.syntaxtree.ConditionNode;
import me.khudyakov.staticanalyzer.components.syntaxtree.StatementListNode;
import me.khudyakov.staticanalyzer.components.syntaxtree.TreeNode;

import java.util.LinkedList;
import java.util.List;

public class SyntaxTree {

    private StatementListNode root;

    private List<TreeNode> dfsOrder;

    public SyntaxTree(StatementListNode root) {
        this.root = root;
        dfsOrder = new LinkedList<>();
        // adding dummy node to link it with first node
        dfsOrder.add(TreeNode.EMPTY_NODE);
        traverseTree(root);
        // removing dummy node
        dfsOrder.remove(0);
    }

    private void traverseTree(TreeNode root) {
        dfsOrder.get(dfsOrder.size() - 1).setNext(root);
        dfsOrder.add(root);
        if(root instanceof StatementListNode) {
            StatementListNode node = (StatementListNode) root;
            node.getStatements()
                .forEach(this::traverseTree);
        } else if(root instanceof ConditionNode) {
            ConditionNode node = (ConditionNode) root;
            traverseTree(node.getBody());
        }
    }

    public int getSize() {
        return dfsOrder.size();
    }

    public StatementListNode getRoot() {
        return root;
    }

    public List<TreeNode> getDfsOrder() {
        return dfsOrder;
    }
}
