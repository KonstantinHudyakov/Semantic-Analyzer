package me.khudyakov.semanticanalyzer.service;

import me.khudyakov.semanticanalyzer.components.semantictree.BlockStatementsNode;
import me.khudyakov.semanticanalyzer.components.semantictree.ConditionNode;
import me.khudyakov.semanticanalyzer.components.semantictree.TreeNode;
import me.khudyakov.semanticanalyzer.program.Program;

import java.util.List;

public class FramingIfFeatureFinder implements FeatureFinder {

    @Override
    public boolean featureFound(Program oldVersion, Program curVersion) {
        List<TreeNode> oldDfsOrder = oldVersion.getSemanticTree().getDfsOrder();
        List<TreeNode> curDfsOrder = curVersion.getSemanticTree().getDfsOrder();

        int size = oldDfsOrder.size();
        boolean found = false;
        for(int i = 0; i < size - 1 && !found; i++) {
            TreeNode cur = curDfsOrder.get(i);
            TreeNode old = oldDfsOrder.get(i);
            if(cur instanceof ConditionNode && old instanceof ConditionNode) {
                if(curDfsOrder.get(i + 1) instanceof BlockStatementsNode && !(oldDfsOrder.get(i + 1) instanceof BlockStatementsNode)) {
                    found = true;
                }
            }
        }
        return found;
    }
}
