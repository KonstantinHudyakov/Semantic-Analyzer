package me.khudyakov.staticanalyzer.entity.syntaxtree;

import me.khudyakov.staticanalyzer.entity.syntaxtree.statement.BlockStatement;
import me.khudyakov.staticanalyzer.entity.syntaxtree.statement.IfStatement;
import me.khudyakov.staticanalyzer.entity.syntaxtree.statement.Statement;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SyntaxTree {

    public static final SyntaxTree EMPTY_TREE = new SyntaxTree(new BlockStatement(new ArrayList<>(), 0, -1));

    private final BlockStatement root;
    private final List<Statement> dfsOrder;

    public SyntaxTree(BlockStatement root) {
        this.root = root;
        dfsOrder = new LinkedList<>();
        // adding dummy node to link it with first node
        dfsOrder.add(Statement.EMPTY_STATEMENT);
        traverseTree(root);
        // removing dummy node
        dfsOrder.remove(0);
    }

    private void traverseTree(Statement root) {
        dfsOrder.get(dfsOrder.size() - 1).setNext(root);
        dfsOrder.add(root);
        if(root instanceof BlockStatement) {
            BlockStatement node = (BlockStatement) root;
            node.stream()
                .forEach(this::traverseTree);
        } else if(root instanceof IfStatement) {
            IfStatement node = (IfStatement) root;
            traverseTree(node.getBody());
        }
    }

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
