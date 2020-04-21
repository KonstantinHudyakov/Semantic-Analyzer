package me.khudyakov.staticanalyzer.service;

import me.khudyakov.staticanalyzer.entity.syntaxtree.statement.BlockStatement;
import me.khudyakov.staticanalyzer.entity.syntaxtree.statement.IfStatement;
import me.khudyakov.staticanalyzer.entity.syntaxtree.SyntaxTreeChange;
import me.khudyakov.staticanalyzer.entity.syntaxtree.SyntaxTreeChange.ChangeType;

import java.util.List;

public class FramingIfChangeSequenceFinder implements FramingIfFinder {

    @Override
    public boolean featureFound(SyntaxTreeChangesCache syntaxTreeChangesCache) {
        List<SyntaxTreeChange> lastChangeSequence = syntaxTreeChangesCache.getLastChangeSequence();
        if (lastChangeSequence.size() == 2) {
            SyntaxTreeChange firstChange = lastChangeSequence.get(0);
            SyntaxTreeChange lastChange = lastChangeSequence.get(1);
            if (firstChange.getChangeType() == ChangeType.INSERT
                    && lastChange.getChangeType() == ChangeType.INSERT
                    && firstChange.getChangedNode() instanceof IfStatement
                    && lastChange.getChangedNode() instanceof BlockStatement) {
                // inserted if(...) { ... }
                BlockStatement block = (BlockStatement) lastChange.getChangedNode();
                IfStatement ifStatement = (IfStatement) firstChange.getChangedNode();

                // check that block is not empty
                if(block.getChildren().size() == 0) return false;
                if(ifStatement.getBody() instanceof BlockStatement) {
                    BlockStatement ifBody = (BlockStatement) ifStatement.getBody();
                    // check that new block not added around old block
                    return ifBody.getEndInd() - ifBody.getStartInd() != block.getEndInd() - block.getStartInd() - 2;
                }
                return true;
            }
        }
        return false;
    }
}
