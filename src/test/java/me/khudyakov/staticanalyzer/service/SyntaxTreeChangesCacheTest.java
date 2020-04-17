package me.khudyakov.staticanalyzer.service;

import me.khudyakov.staticanalyzer.components.syntaxtree.TreeNode;
import me.khudyakov.staticanalyzer.program.SyntaxTree;
import me.khudyakov.staticanalyzer.program.SyntaxTreeChange;
import me.khudyakov.staticanalyzer.util.ExpressionConverterException;
import me.khudyakov.staticanalyzer.util.ExpressionExecutionException;
import me.khudyakov.staticanalyzer.util.SyntaxAnalyzerException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.util.List;

import static me.khudyakov.staticanalyzer.program.SyntaxTreeChange.ChangeType;
import static me.khudyakov.staticanalyzer.service.ServiceUtils.addChange;
import static me.khudyakov.staticanalyzer.service.ServiceUtils.addChangeSequence;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SyntaxTreeChangesCacheTest {

    private SyntaxTreeChangesCache cache;

    private final String text1 = "@x = 3 * 7;";
    private final String text2 = "if((3 + 4) * 2 > 0) @x = 3 * 7;";
    private final String text3 = "if((3 + 4) * 2 > 0) { @x = 3 * 7; }";

    @BeforeEach
    private void tearUp() {
        cache = new SyntaxTreeChangesCache(3);
    }

    @Test
    void addStatement() throws ParseException, ExpressionConverterException, ExpressionExecutionException, SyntaxAnalyzerException {
        SyntaxTree syntaxTree = addChange(cache, text1);

        assertEquals(2, cache.size());

        SyntaxTreeChange lastChange = checkSequenceSizeAndGetLastChange(1, 0);
        TreeNode addedNode = syntaxTree.getDfsOrder().get(1);
        assertEquals(ChangeType.ADD, lastChange.getChangeType());
        assertEquals(addedNode, lastChange.getChangedNode());
    }

    @Test
    void insertStatement() throws ExpressionConverterException, ExpressionExecutionException, SyntaxAnalyzerException, ParseException {
        List<SyntaxTree> syntaxTreeList = addChangeSequence(cache, text1, text2);
        SyntaxTree lastTree = syntaxTreeList.get(1);

        assertEquals(3, cache.size());

        SyntaxTreeChange lastChange = checkSequenceSizeAndGetLastChange(1, 0);
        TreeNode addedNode = lastTree.getDfsOrder().get(1);
        assertEquals(ChangeType.INSERT, lastChange.getChangeType());
        assertEquals(addedNode, lastChange.getChangedNode());
    }

    @Test
    void insertBlockStatement() throws ExpressionConverterException, ExpressionExecutionException, SyntaxAnalyzerException, ParseException {
        List<SyntaxTree> syntaxTreeList = addChangeSequence(cache, text1, text2, text3);
        SyntaxTree lastTree = syntaxTreeList.get(2);

        assertEquals(3, cache.size());

        SyntaxTreeChange lastChange = checkSequenceSizeAndGetLastChange(2, 1);
        TreeNode addedNode = lastTree.getDfsOrder().get(2);
        assertEquals(ChangeType.INSERT, lastChange.getChangeType());
        assertEquals(addedNode, lastChange.getChangedNode());
    }

    @Test
    void removeBlockStatement() throws ExpressionConverterException, ExpressionExecutionException, SyntaxAnalyzerException, ParseException {
        List<SyntaxTree> syntaxTreeList = addChangeSequence(cache, text1, text2, text3, text2);
        SyntaxTree tree = syntaxTreeList.get(2);

        assertEquals(3, cache.size());

        SyntaxTreeChange lastChange = checkSequenceSizeAndGetLastChange(1, 0);
        TreeNode addedNode = tree.getDfsOrder().get(2);
        assertEquals(ChangeType.REMOVE, lastChange.getChangeType());
        assertEquals(addedNode, lastChange.getChangedNode());
    }

    private SyntaxTreeChange checkSequenceSizeAndGetLastChange(int expectedSize, int index) {
        List<SyntaxTreeChange> lastChangeSequence = cache.getLastChangeSequence();
        assertEquals(expectedSize, lastChangeSequence.size());
        return lastChangeSequence.get(index);
    }


}