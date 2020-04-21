package me.khudyakov.staticanalyzer.service;

import javafx.util.Pair;
import me.khudyakov.staticanalyzer.entity.syntaxtree.SyntaxTree;
import me.khudyakov.staticanalyzer.entity.syntaxtree.statement.Statement;
import me.khudyakov.staticanalyzer.entity.syntaxtree.SyntaxTreeChange;
import me.khudyakov.staticanalyzer.entity.syntaxtree.SyntaxTreeChange.ChangeType;
import me.khudyakov.staticanalyzer.util.TreeUtils;

import java.util.*;

public class SyntaxTreeChangesCache {

    private final Deque<List<SyntaxTreeChange>> changes = new LinkedList<>();
    private final List<SyntaxTree> syntaxTreeVersions = new LinkedList<>();
    private final int maxCacheSize;

    public SyntaxTreeChangesCache(int maxCacheSize) {
        this.maxCacheSize = maxCacheSize;
        syntaxTreeVersions.add(SyntaxTree.EMPTY_TREE);
    }

    public boolean addNewChange(SyntaxTree curTree) {
        SyntaxTree prevTree = getLastSyntaxTree();
        Pair<Statement, Statement> diff = TreeUtils.findDiff(prevTree, curTree);
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
        addNewSyntaxTreeVersion(curTree);
        return true;
    }

    public List<SyntaxTreeChange> getLastChangeSequence() {
        return changes.getLast();
    }

    public SyntaxTree getLastSyntaxTree() {
        return syntaxTreeVersions.get(syntaxTreeVersions.size() - 1);
    }

    /**
     * @param num - number of SyntaxTree from newest to oldest (0 - last version of tree)
     * @return SyntaxTree of desired version from newest to oldest
     */
    public SyntaxTree getSyntaxTree(int num) {
        return syntaxTreeVersions.get(syntaxTreeVersions.size() - num - 1);
    }

    public int changesSize() {
        return changes.size();
    }

    public int treeVersionsSize() {
        return syntaxTreeVersions.size();
    }

    public int getMaxCacheSize() {
        return maxCacheSize;
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

    private void addNewSyntaxTreeVersion(SyntaxTree tree) {
        Pair<Statement, Statement> diff = TreeUtils.findDiff(getLastSyntaxTree(), tree);
        if(diff.getKey() != Statement.EMPTY_STATEMENT || diff.getValue() != Statement.EMPTY_STATEMENT) {
            syntaxTreeVersions.add(tree);
            if(syntaxTreeVersions.size() > maxCacheSize) {
                syntaxTreeVersions.remove(0);
            }
        }
    }
}
