package me.khudyakov.staticanalyzer.service;

import me.khudyakov.staticanalyzer.components.syntaxtree.BlockStatementsNode;
import me.khudyakov.staticanalyzer.components.syntaxtree.ConditionNode;
import me.khudyakov.staticanalyzer.components.syntaxtree.TreeNode;
import me.khudyakov.staticanalyzer.program.Program;

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
