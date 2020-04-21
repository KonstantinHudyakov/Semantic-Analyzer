package me.khudyakov.staticanalyzer.service;

import javafx.util.Pair;
import me.khudyakov.staticanalyzer.entity.syntaxtree.SyntaxTree;
import me.khudyakov.staticanalyzer.entity.syntaxtree.statement.BlockStatement;
import me.khudyakov.staticanalyzer.entity.syntaxtree.statement.IfStatement;
import me.khudyakov.staticanalyzer.entity.syntaxtree.statement.Statement;
import me.khudyakov.staticanalyzer.util.TreeUtils;

public class FramingIfTwoStepFinder implements FramingIfFinder {

    @Override
    public boolean featureFound(SyntaxTreeChangesCache cache) {
        if(cache.treeVersionsSize() < 3) {
            return false;
        }
        SyntaxTree curTree = cache.getLastSyntaxTree();
        // SyntaxTree before two modifications
        SyntaxTree oldTree = cache.getSyntaxTree(2);
        if(curTree.getSize() - oldTree.getSize() != 2) {
            // we need to check only two inserts
            return false;
        }

        Pair<Statement, Statement> diff = TreeUtils.findDiff(oldTree, curTree);
        Statement oldVersion = diff.getKey();
        Statement curVersion = diff.getValue();
        if(oldVersion == Statement.EMPTY_STATEMENT) {
            // it means that two statements added to the end of program
            // it is not our case because if new statements is IfStatement and Block,
            // block will be empty
            return false;
        }

        if(curVersion instanceof IfStatement) {
            IfStatement ifStatement = (IfStatement) curVersion;
            if(ifStatement.getBody() instanceof BlockStatement) {
                BlockStatement block = (BlockStatement) ifStatement.getBody();
                if(block.size() == 0 || block.size() == 1 && block.next() instanceof BlockStatement) {
                    return false;
                }
                Statement cur = block.next();
                boolean contentEqual = true;
                int curInd = 0;
                while(curInd < block.size() && contentEqual && oldVersion != null) {
                    if(!cur.contentEquals(oldVersion) || cur.getChildren().size() != oldVersion.getChildren().size()) {
                        contentEqual = false;
                    }
                    oldVersion = oldVersion.next();
                    cur = cur.next();
                    curInd++;
                }

                return contentEqual && curInd == block.size();
            }
        }

        return false;
    }
}
