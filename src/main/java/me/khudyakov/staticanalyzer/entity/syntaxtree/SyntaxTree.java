package me.khudyakov.staticanalyzer.entity.syntaxtree;

import me.khudyakov.staticanalyzer.entity.syntaxtree.statement.BlockStatement;
import me.khudyakov.staticanalyzer.entity.syntaxtree.statement.Statement;

import java.util.List;

public class SyntaxTree {

    private final BlockStatement root;

    private final List<Statement> dfsOrder;

    public SyntaxTree(BlockStatement root, List<Statement> dfsOrder) {
        this.root = root;
        this.dfsOrder = dfsOrder;
    }

    //    public SyntaxTree(BlockStatement root) {
//        this.root = root;
//        dfsOrder = new LinkedList<>();
//        // adding dummy node to link it with first node
//        dfsOrder.add(TreeNode.EMPTY_NODE);
//        traverseTree(root);
//        // removing dummy node
//        dfsOrder.remove(0);
//    }
//
//    private void traverseTree(TreeNode root) {
//        dfsOrder.get(dfsOrder.size() - 1).setNext(root);
//        dfsOrder.add(root);
//        if(root instanceof StatementListNode) {
//            StatementListNode node = (StatementListNode) root;
//            node.getStatements()
//                .forEach(this::traverseTree);
//        } else if(root instanceof ConditionNode) {
//            ConditionNode node = (ConditionNode) root;
//            traverseTree(node.getBody());
//        }
//    }

    public int getSize() {
        return dfsOrder.size();
    }

    public BlockStatement getRoot() {
        return root;
    }

    public List<Statement> getDfsOrder() {
        return dfsOrder;
    }
}
