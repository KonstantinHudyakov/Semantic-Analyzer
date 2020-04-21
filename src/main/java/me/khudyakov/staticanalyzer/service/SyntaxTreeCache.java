package me.khudyakov.staticanalyzer.service;

import javafx.util.Pair;
import me.khudyakov.staticanalyzer.entity.syntaxtree.SyntaxTree;
import me.khudyakov.staticanalyzer.entity.syntaxtree.statement.Statement;
import me.khudyakov.staticanalyzer.util.TreeUtils;

import java.util.LinkedList;
import java.util.List;

public class SyntaxTreeCache {

    private final List<SyntaxTree> syntaxTreeVersions = new LinkedList<>();
    private final int maxCacheSize;

    public SyntaxTreeCache(int maxCacheSize) {
        this.maxCacheSize = maxCacheSize;
        syntaxTreeVersions.add(SyntaxTree.EMPTY_TREE);
    }

    /**
     * @return true if new version not equal last version of SyntaxTree
     */
    public boolean addNewSyntaxTreeVersion(SyntaxTree tree) {
        Pair<Statement, Statement> diff = TreeUtils.findDiff(getLastSyntaxTree(), tree);
        if(diff.getKey() != Statement.EMPTY_STATEMENT || diff.getValue() != Statement.EMPTY_STATEMENT) {
            syntaxTreeVersions.add(tree);
            if(syntaxTreeVersions.size() > maxCacheSize) {
                syntaxTreeVersions.remove(0);
            }
            return true;
        }
        return false;
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

    public int size() {
        return syntaxTreeVersions.size();
    }

    public int getMaxCacheSize() {
        return maxCacheSize;
    }
}
