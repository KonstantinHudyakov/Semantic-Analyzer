package me.khudyakov.staticanalyzer.service;

import javafx.util.Pair;
import me.khudyakov.staticanalyzer.components.syntaxtree.ConditionNode;
import me.khudyakov.staticanalyzer.components.syntaxtree.StatementListNode;
import me.khudyakov.staticanalyzer.components.syntaxtree.TreeNode;
import me.khudyakov.staticanalyzer.program.SyntaxTree;
import me.khudyakov.staticanalyzer.program.SyntaxTreeChange;
import me.khudyakov.staticanalyzer.program.SyntaxTreeChange.ChangeType;

import java.util.*;

public class SyntaxTreeChangesCache {

    private final Deque<List<SyntaxTreeChange>> changes = new LinkedList<>();

    private final int maxCacheSize;

    private SyntaxTree prevTree = new SyntaxTree(new StatementListNode());

    public SyntaxTreeChangesCache(int maxCacheSize) {
        this.maxCacheSize = maxCacheSize;
        init();
    }

    private void init() {
        List<SyntaxTreeChange> dummy = new ArrayList<>();
        dummy.add(SyntaxTreeChange.NOTHING_CHANGED);
        changes.addLast(dummy);
    }

    //if(2 > 1) {
    //	if(4 > 3) {
    //		@x = 3;
    //		x + x;
    //	}
    //	@y = 5;
    //}
    //x * y;

    public boolean addNewChange(SyntaxTree curTree) {
        Pair<TreeNode, TreeNode> diff = findDiff(prevTree, curTree);
        TreeNode prevVersion = diff.getKey();
        TreeNode curVersion = diff.getValue();
        SyntaxTreeChange newChange = null;
        if (prevVersion == TreeNode.EMPTY_NODE && curVersion == TreeNode.EMPTY_NODE) {
            // nothing changed
            return false;
        } else if (prevVersion == TreeNode.EMPTY_NODE) {
            // new statement added to the end of program
            newChange = new SyntaxTreeChange(curVersion, ChangeType.ADD);
            addNewChangeSequence(newChange);
        } else if (curVersion == TreeNode.EMPTY_NODE) {
            // statement removed from the end of program
            newChange = new SyntaxTreeChange(prevVersion, ChangeType.REMOVE);
            addNewChangeSequence(newChange);
        } else if (curTree.getSize() - prevTree.getSize() == 1) {
            // new statement inserted
            handleInserting(prevVersion, curVersion);
        } else if (curTree.getSize() - prevTree.getSize() == -1) {
            // statement removed
            newChange = new SyntaxTreeChange(prevVersion, ChangeType.REMOVE);
            addNewChangeSequence(newChange);
        } else if (prevVersion.getClass() == curVersion.getClass()) {
            // same node types - statement is edited
            newChange = new SyntaxTreeChange(curVersion, ChangeType.EDIT);
            addNewChangeSequence(newChange);
        } else {
            // some multiple changes occurred
            changes.clear();
            init();
        }
        prevTree = curTree;
        return true;
    }

    public List<SyntaxTreeChange> getLastChangeSequence() {
        return changes.getLast();
    }

    public int size() {
        return changes.size();
    }

    private Pair<TreeNode, TreeNode> findDiff(SyntaxTree prevTree, SyntaxTree curTree) {
        Stack<TreeNode> stCur = new Stack<>();
        Stack<TreeNode> stPrev = new Stack<>();
        addStatementsToStack(stCur, curTree.getRoot());
        addStatementsToStack(stPrev, prevTree.getRoot());

        while (!stPrev.isEmpty() && !stCur.isEmpty()) {
            TreeNode prev = stPrev.pop();
            TreeNode cur = stCur.pop();
            if (!prev.contentEquals(cur)) {
                return new Pair<>(prev, cur);
            }
            if (cur instanceof StatementListNode) {
                addStatementsToStack(stPrev, (StatementListNode) prev);
                addStatementsToStack(stCur, (StatementListNode) cur);
            } else if (cur instanceof ConditionNode) {
                stPrev.push(((ConditionNode) prev).getBody());
                stCur.push(((ConditionNode) cur).getBody());
            }
        }

        if (stPrev.isEmpty() && stCur.isEmpty()) {
            return new Pair<>(TreeNode.EMPTY_NODE, TreeNode.EMPTY_NODE);
        } else if (stPrev.isEmpty()) {
            return new Pair<>(TreeNode.EMPTY_NODE, stCur.peek());
        } else {
            return new Pair<>(stPrev.peek(), TreeNode.EMPTY_NODE);
        }
    }

    private void handleInserting(TreeNode prevVersion, TreeNode curVersion) {
        SyntaxTreeChange newChange = new SyntaxTreeChange(curVersion, ChangeType.INSERT);

        List<SyntaxTreeChange> lastChangeSequence = changes.getLast();
        SyntaxTreeChange lastChange = lastChangeSequence.get(lastChangeSequence.size() - 1);
        TreeNode lastChangedNode = lastChange.getChangedNode();
        if(lastChangedNode.getNext() == prevVersion) { // lastChangedNode.getEndInd() + 1 == curVersion.getStartInd()
            lastChangeSequence.add(newChange);
        } else {
            addNewChangeSequence(newChange);
        }
    }

    private void addNewChangeSequence(SyntaxTreeChange newChange) {
        List<SyntaxTreeChange> newChangeSequence = new ArrayList<>();
        newChangeSequence.add(newChange);
        changes.addLast(newChangeSequence);
        if (changes.size() > maxCacheSize) {
            changes.pollFirst();
        }
    }

    private void addStatementsToStack(Stack<TreeNode> stack, StatementListNode node) {
        List<TreeNode> nodes = node.getStatements();
        for (int i = nodes.size() - 1; i >= 0; i--) {
            stack.push(nodes.get(i));
        }
    }

//    private ChangeType getChangeType(SyntaxTree prev, SyntaxTree cur) {
//        int sizeDiff = cur.getSize() - prev.getSize();
//        switch (sizeDiff) {
//            case 0:
//                return ChangeType.EDIT;
//            case 1:
//                return ChangeType.INSERT;
//            case -1:
//                return ChangeType.REMOVE;
//            default:
//                return ChangeType.MULTIPLE_CHANGES;
//        }
//    }
}
