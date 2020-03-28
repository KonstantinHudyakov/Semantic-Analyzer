package me.khudyakov.semanticanalyzer.program;

import me.khudyakov.semanticanalyzer.components.semantictree.ConditionNode;
import me.khudyakov.semanticanalyzer.components.semantictree.StatementListNode;
import me.khudyakov.semanticanalyzer.components.semantictree.TreeNode;

import java.util.ArrayList;
import java.util.List;

public class SemanticTree {

    private StatementListNode root;

    private List<TreeNode> dfsOrder;

    public SemanticTree(StatementListNode root) {
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
