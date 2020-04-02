package me.khudyakov.semanticanalyzer.service;

import me.khudyakov.semanticanalyzer.components.syntaxtree.BlockStatementsNode;
import me.khudyakov.semanticanalyzer.components.syntaxtree.ConditionNode;
import me.khudyakov.semanticanalyzer.components.syntaxtree.TreeNode;
import me.khudyakov.semanticanalyzer.program.Program;

import java.util.List;

public class FramingIfFeatureFinder implements FeatureFinder {

    @Override
    public boolean featureFound(Program oldVersion, Program curVersion) {
        List<TreeNode> oldDfsOrder = oldVersion.getSyntaxTree().getDfsOrder();
        List<TreeNode> curDfsOrder = curVersion.getSyntaxTree().getDfsOrder();

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
