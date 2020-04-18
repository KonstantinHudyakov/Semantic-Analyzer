package me.khudyakov.staticanalyzer.service;

import javafx.util.Pair;
import me.khudyakov.staticanalyzer.entity.syntaxtree.SyntaxTree;
import me.khudyakov.staticanalyzer.entity.syntaxtree.statement.Statement;
import me.khudyakov.staticanalyzer.entity.syntaxtree.SyntaxTreeChange;
import me.khudyakov.staticanalyzer.entity.syntaxtree.SyntaxTreeChange.ChangeType;

import java.util.*;

public class SyntaxTreeChangesCache {

    private final Deque<List<SyntaxTreeChange>> changes = new LinkedList<>();
    private final int maxCacheSize;

    private SyntaxTree prevTree = SyntaxTree.EMPTY_TREE;

    public SyntaxTreeChangesCache(int maxCacheSize) {
        this.maxCacheSize = maxCacheSize;
    }

    public boolean addNewChange(SyntaxTree curTree) {
        Pair<Statement, Statement> diff = findDiff(prevTree, curTree);
        Statement prevVersion = diff.getKey();
        Statement curVersion = diff.getValue();
        SyntaxTreeChange newChange;
        if (prevVersion == Statement.EMPTY_STATEMENT && curVersion == Statement.EMPTY_STATEMENT) {
            // nothing changed
            return false;
        } else if (curTree.getSize() - prevTree.getSize() == 1) {
            // new statement inserted
            handleInserting(curVersion);
        } else if (curTree.getSize() - prevTree.getSize() == -1) {
            // statement removed
            newChange = new SyntaxTreeChange(prevVersion, ChangeType.REMOVE);
            addNewChangeSequence(newChange);
        } else if (curTree.getSize() == prevTree.getSize()
                && prevVersion.getClass() == curVersion.getClass()) {
            // same node types - statement is edited
            newChange = new SyntaxTreeChange(curVersion, ChangeType.EDIT);
            addNewChangeSequence(newChange);
        } else {
            // some multiple changes occurred
            changes.clear();
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

    private Pair<Statement, Statement> findDiff(SyntaxTree prevTree, SyntaxTree curTree) {
        Stack<Statement> stCur = new Stack<>();
        Stack<Statement> stPrev = new Stack<>();
        addStatementsToStack(stCur, curTree.getRoot().getChildren());
        addStatementsToStack(stPrev, prevTree.getRoot().getChildren());

        while (!stPrev.isEmpty() && !stCur.isEmpty()) {
            Statement prev = stPrev.pop();
            Statement cur = stCur.pop();
            if (!prev.contentEquals(cur)) {
                return new Pair<>(prev, cur);
            }
            addStatementsToStack(stCur, cur.getChildren());
            addStatementsToStack(stPrev, prev.getChildren());
        }

        if (stPrev.isEmpty() && stCur.isEmpty()) {
            return new Pair<>(Statement.EMPTY_STATEMENT, Statement.EMPTY_STATEMENT);
        } else if (stPrev.isEmpty()) {
            return new Pair<>(Statement.EMPTY_STATEMENT, stCur.peek());
        } else {
            return new Pair<>(stPrev.peek(), Statement.EMPTY_STATEMENT);
        }
    }

    private void handleInserting(Statement curVersion) {
        SyntaxTreeChange newChange = new SyntaxTreeChange(curVersion, ChangeType.INSERT);
        if(changes.isEmpty()) {
            addNewChangeSequence(newChange);
            return;
        }

        List<SyntaxTreeChange> lastChangeSequence = changes.getLast();
        SyntaxTreeChange lastChange = lastChangeSequence.get(lastChangeSequence.size() - 1);
        Statement lastChangedNode = lastChange.getChangedNode();
        if(lastChangedNode.getEndInd() + 1 == curVersion.getStartInd()) {
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

    private void addStatementsToStack(Stack<Statement> stack, List<? extends Statement> statements) {
        for (int i = statements.size() - 1; i >= 0; i--) {
            stack.push(statements.get(i));
        }
    }
}
