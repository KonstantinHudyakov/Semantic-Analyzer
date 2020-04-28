package me.khudyakov.staticanalyzer.service;

import javafx.util.Pair;
import me.khudyakov.staticanalyzer.entity.syntaxtree.SyntaxTree;
import me.khudyakov.staticanalyzer.entity.syntaxtree.statement.BlockStatement;
import me.khudyakov.staticanalyzer.entity.syntaxtree.statement.IfStatement;
import me.khudyakov.staticanalyzer.entity.syntaxtree.statement.Statement;
import me.khudyakov.staticanalyzer.util.TreeUtils;

public class FramingIfFinder implements FeatureFinder {

    @Override
    public boolean featureFound(SyntaxTreeCache cache) {
        if (cache.size() < 3) {
            return false;
        }
        SyntaxTree curTree = cache.getLastSyntaxTree();
        // previous version of SyntaxTree
        SyntaxTree prevTree = cache.getSyntaxTree(1);
        // SyntaxTree before two modifications
        SyntaxTree oldTree = cache.getSyntaxTree(2);
        if (curTree.getSize() - oldTree.getSize() != 2) {
            // we need to check only two inserts
            return false;
        }

        Pair<Statement, Statement> curOldDiff = TreeUtils.findDiff(oldTree, curTree);
        Pair<Statement, Statement> curPrevDiff = TreeUtils.findDiff(prevTree, curTree);
        // verify that IfStatement is added first and then BlockStatement
        if (!(curOldDiff.getValue() instanceof IfStatement)
                || !(curPrevDiff.getValue() instanceof BlockStatement)) {
            return false;
        }

        // node that been here before modifications and inserted ifStatement
        Statement oldVersion = curOldDiff.getKey();
        IfStatement ifStatement = (IfStatement) curOldDiff.getValue();

        if (ifStatement.getBody() instanceof BlockStatement) {
            BlockStatement block = (BlockStatement) ifStatement.getBody();
            // check that framing block is not empty
            // or added block that framed block from previous version
            if (block.size() == 0 || block.size() == 1 && block.next() instanceof BlockStatement) {
                return false;
            }
            return checkNodesAfterBlockEqual(oldVersion, block);
        }
        return false;
    }

    private boolean checkNodesAfterBlockEqual(Statement oldNode, BlockStatement insertedBlock) {
        int curInd = 0;
        boolean contentEqual = true;
        Statement curNode = insertedBlock.next();

        while (curInd < insertedBlock.size() && contentEqual && oldNode != null) {
            if (!isNodesEqual(curNode, oldNode)) {
                contentEqual = false;
            }
            oldNode = oldNode.next();
            curNode = curNode.next();
            curInd++;
        }
        return contentEqual && curInd == insertedBlock.size();
    }

    private boolean isNodesEqual(Statement st1, Statement st2) {
        return st1 != null
                && st2 != null
                && st1.contentEquals(st2)
                && st1.getChildren().size() == st2.getChildren().size();
    }
}
