package me.khudyakov.staticanalyzer.service;

import me.khudyakov.staticanalyzer.entity.syntaxtree.SyntaxTree;
import me.khudyakov.staticanalyzer.entity.syntaxtree.statement.Statement;
import me.khudyakov.staticanalyzer.entity.syntaxtree.SyntaxTreeChange;
import me.khudyakov.staticanalyzer.entity.syntaxtree.SyntaxTreeChange.ChangeType;
import me.khudyakov.staticanalyzer.util.SyntaxAnalyzerException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.util.List;

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
    void addFirstStatement() throws ParseException, SyntaxAnalyzerException {
        SyntaxTree syntaxTree = addChange(cache, text1);

        assertEquals(1, cache.changesSize());

        SyntaxTreeChange lastChange = checkSequenceSizeAndGetLastChange(1, 0);
        Statement addedNode = syntaxTree.getDfsOrder().get(1);
        assertEquals(ChangeType.INSERT, lastChange.getChangeType());
        assertEquals(addedNode, lastChange.getChangedNode());
    }

    @Test
    void insertStatement() throws SyntaxAnalyzerException, ParseException {
        List<SyntaxTree> syntaxTreeList = addChangeSequence(cache, text1, text2);
        SyntaxTree lastTree = syntaxTreeList.get(1);

        assertEquals(2, cache.changesSize());

        SyntaxTreeChange lastChange = checkSequenceSizeAndGetLastChange(1, 0);
        Statement addedNode = lastTree.getDfsOrder().get(1);
        assertEquals(ChangeType.INSERT, lastChange.getChangeType());
        assertEquals(addedNode, lastChange.getChangedNode());
    }

    @Test
    void insertBlockStatement() throws SyntaxAnalyzerException, ParseException {
        List<SyntaxTree> syntaxTreeList = addChangeSequence(cache, text1, text2, text3);
        SyntaxTree lastTree = syntaxTreeList.get(2);

        assertEquals(2, cache.changesSize());

        SyntaxTreeChange lastChange = checkSequenceSizeAndGetLastChange(2, 1);
        Statement addedNode = lastTree.getDfsOrder().get(2);
        assertEquals(ChangeType.INSERT, lastChange.getChangeType());
        assertEquals(addedNode, lastChange.getChangedNode());
    }

    @Test
    void removeBlockStatement() throws SyntaxAnalyzerException, ParseException {
        List<SyntaxTree> syntaxTreeList = addChangeSequence(cache, text1, text2, text3, text2);
        SyntaxTree tree = syntaxTreeList.get(2);

        assertEquals(3, cache.changesSize());

        SyntaxTreeChange lastChange = checkSequenceSizeAndGetLastChange(1, 0);
        Statement addedNode = tree.getDfsOrder().get(2);
        assertEquals(ChangeType.REMOVE, lastChange.getChangeType());
        assertEquals(addedNode, lastChange.getChangedNode());
    }

    private SyntaxTreeChange checkSequenceSizeAndGetLastChange(int expectedSize, int index) {
        List<SyntaxTreeChange> lastChangeSequence = cache.getLastChangeSequence();
        assertEquals(expectedSize, lastChangeSequence.size());
        return lastChangeSequence.get(index);
    }
}