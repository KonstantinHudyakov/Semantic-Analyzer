package me.khudyakov.staticanalyzer.program;

import me.khudyakov.staticanalyzer.components.syntaxtree.ConditionNode;
import me.khudyakov.staticanalyzer.components.syntaxtree.StatementListNode;
import me.khudyakov.staticanalyzer.components.syntaxtree.TreeNode;

import java.util.ArrayList;
import java.util.List;

public class SyntaxTree {

    private StatementListNode root;

    private List<TreeNode> dfsOrder;

    public SyntaxTree(StatementListNode root) {
        this.root = root;
        dfsOrder = new ArrayList<>();
        traverseTree(root);
    }

    private void traverseTree(TreeNode root) {
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

    public StatementListNode getRoot() {
        return root;
    }

    public List<TreeNode> getDfsOrder() {
        return dfsOrder;
    }
}
