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
        // SyntaxTree before two modifications
        SyntaxTree oldTree = cache.getSyntaxTree(2);
        if (curTree.getSize() - oldTree.getSize() != 2) {
            // we need to check only two inserts
            return false;
        }

        Pair<Statement, Statement> diff = TreeUtils.findDiff(oldTree, curTree);
        Statement oldVersion = diff.getKey();
        Statement curVersion = diff.getValue();
        if (oldVersion == Statement.EMPTY_STATEMENT) {
            // it means that two statements added to the end of program
            // it is not our case because if new statements is IfStatement and Block,
            // block will be empty
            return false;
        }

        // check that inserted construction if(...) { FramedStatements }
        // and FramedStatements content equals previous version's statements content
        if (curVersion instanceof IfStatement) {
            IfStatement ifStatement = (IfStatement) curVersion;
            if (ifStatement.getBody() instanceof BlockStatement) {
                BlockStatement block = (BlockStatement) ifStatement.getBody();
                // check that framing block is not empty
                // or added block that framed block from previous version
                if (block.size() == 0 || block.size() == 1 && block.next() instanceof BlockStatement) {
                    return false;
                }
                return checkNodesAfterBlockEqual(oldVersion, block);
            }
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
