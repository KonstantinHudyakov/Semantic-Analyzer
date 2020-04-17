package me.khudyakov.staticanalyzer.service;

import me.khudyakov.staticanalyzer.components.syntaxtree.BlockStatementsNode;
import me.khudyakov.staticanalyzer.components.syntaxtree.ConditionNode;
import me.khudyakov.staticanalyzer.program.SyntaxTreeChange;
import me.khudyakov.staticanalyzer.program.SyntaxTreeChange.ChangeType;

import java.util.List;

public class FramingIfFeatureFinder implements FeatureFinder {
    @Override
    public boolean featureFound(SyntaxTreeChangesCache syntaxTreeChangesCache) {
        List<SyntaxTreeChange> lastChangeSequence = syntaxTreeChangesCache.getLastChangeSequence();
        if (lastChangeSequence.size() == 2) {
            SyntaxTreeChange firstChange = lastChangeSequence.get(0);
            SyntaxTreeChange lastChange = lastChangeSequence.get(1);
            if(firstChange.getChangeType() == ChangeType.INSERT
                    && lastChange.getChangeType() == ChangeType.INSERT
                    && firstChange.getChangedNode() instanceof ConditionNode
                    && lastChange.getChangedNode() instanceof BlockStatementsNode) {
                // inserted if(...) { ... }
                // check that block is not empty
                BlockStatementsNode block = (BlockStatementsNode) lastChange.getChangedNode();
                return block.getStatements().size() > 0;
            }
        }
        return false;
    }

    //    @Override
//    public boolean featureFound(Program oldVersion, Program curVersion) {
//        List<TreeNode> oldDfsOrder = oldVersion.getSyntaxTree().getDfsOrder();
//        List<TreeNode> curDfsOrder = curVersion.getSyntaxTree().getDfsOrder();
//
//        int size = oldDfsOrder.size();
//        boolean found = false;
//        for(int i = 0; i < size - 1 && !found; i++) {
//            TreeNode cur = curDfsOrder.get(i);
//            TreeNode old = oldDfsOrder.get(i);
//            if(cur instanceof ConditionNode && old instanceof ConditionNode) {
//                if(curDfsOrder.get(i + 1) instanceof BlockStatementsNode && !(oldDfsOrder.get(i + 1) instanceof BlockStatementsNode)) {
//                    found = true;
//                }
//            }
//        }
//        return found;
//    }
}
