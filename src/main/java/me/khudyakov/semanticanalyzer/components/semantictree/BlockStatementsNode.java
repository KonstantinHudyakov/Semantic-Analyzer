package me.khudyakov.semanticanalyzer.components.semantictree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class BlockStatementsNode extends TreeNode {

    private List<TreeNode> statements = new ArrayList<>();

    @Override
    public void execute() {
        statements.forEach(TreeNode::execute);
    }

    public List<TreeNode> getStatements() {
        return statements;
    }

    public int size() {
        return statements.size();
    }

    public boolean isEmpty() {
        return statements.isEmpty();
    }

    public boolean contains(Object o) {
        return statements.contains(o);
    }

    public Iterator<TreeNode> iterator() {
        return statements.iterator();
    }

    public boolean add(TreeNode treeNode) {
        return statements.add(treeNode);
    }

    public TreeNode get(int index) {
        return statements.get(index);
    }

    public List<TreeNode> subList(int fromIndex, int toIndex) {
        return statements.subList(fromIndex, toIndex);
    }

    public Stream<TreeNode> stream() {
        return statements.stream();
    }

    public void forEach(Consumer<? super TreeNode> action) {
        statements.forEach(action);
    }
}
