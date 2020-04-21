package me.khudyakov.staticanalyzer.service;

import javafx.util.Pair;
import me.khudyakov.staticanalyzer.entity.syntaxtree.SyntaxTree;
import me.khudyakov.staticanalyzer.entity.syntaxtree.statement.Statement;
import me.khudyakov.staticanalyzer.util.SyntaxAnalyzerException;
import me.khudyakov.staticanalyzer.util.TreeUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.util.List;

import static me.khudyakov.staticanalyzer.service.ServiceUtils.addChangeSequence;
import static me.khudyakov.staticanalyzer.service.ServiceUtils.addChangeSequenceNotThrow;
import static org.junit.jupiter.api.Assertions.*;

class SyntaxTreeCacheTest {

    private SyntaxTreeCache cache;

    @BeforeEach
    private void tearUp() {
        cache = new SyntaxTreeCache(3);
    }

    @Test
    void checkAddedChanges() throws ParseException, SyntaxAnalyzerException {
        String text1 = "@x = 3 * 7;";
        String text2 = "if((3 + 4) * 2 > 0) @x = 3 * 7;";
        String text3 = "if((3 + 4) * 2 > 0) { @x = 3 * 7; }";

        List<SyntaxTree> trees = addChangeSequence(cache, text1, text2, text3);

        assertEquals(3, cache.size());
        checkEqualsTreeInCache(trees);
    }

    @Test
    void checkRemovingStatement() throws ParseException, SyntaxAnalyzerException {
        String text1 = "@x = 3; { x * 45;  } if(3) 6 * 3;";
        String text2 = "@x = 3; {  } if(3) 6 * 3;";

        List<SyntaxTree> trees = addChangeSequence(cache, text1, text2);

        assertEquals(3, cache.size());
        checkEqualsTreeInCache(trees);
    }

    @Test
    void checkEditing() throws ParseException, SyntaxAnalyzerException {
        String text1 = "@xyz = 45*4/2 - 1; xyz - 90;";
        String text2 = "@xyz = 45*4 - 1; xyz - 90;";
        String text3 = "@xyz = 45*4 - 1; xyz;";

        List<SyntaxTree> trees = addChangeSequence(cache, text1, text2, text3);

        assertEquals(3, cache.size());
        checkEqualsTreeInCache(trees);
    }

    @Test
    void checkNotCreateNewVersionAfterMisprint() {
        String text1 = "@y = 5 - 6; if(y > 3) @r = 32; r - 3;";
        String text2 = "@y = 5 - 6; if(y > 3) [ @r = 32; r - 3;";

        List<SyntaxTree> trees = addChangeSequenceNotThrow(cache, text1, text2, text1);

        assertEquals(2, cache.size());
        // check that added only one change
        assertSame(cache.getSyntaxTree(1), SyntaxTree.EMPTY_TREE);

        SyntaxTree cachedVersion = cache.getLastSyntaxTree();
        SyntaxTree curVersion = trees.get(trees.size() - 1);
        assertTrue(isTreeEquals(cachedVersion, curVersion));
    }

    private void checkEqualsTreeInCache(List<SyntaxTree> trees) {
        int n = Math.min(trees.size(), cache.getMaxCacheSize());
        for(int i = 0; i < n; i++) {
            SyntaxTree cachedVersion = cache.getSyntaxTree(i);
            SyntaxTree curVersion = trees.get(trees.size() - 1 - i);
            assertTrue(isTreeEquals(cachedVersion, curVersion));
        }
    }

    private boolean isTreeEquals(SyntaxTree fstTree, SyntaxTree secTree) {
        Pair<Statement, Statement> diff = TreeUtils.findDiff(fstTree, secTree);
        return diff.getKey() == Statement.EMPTY_STATEMENT
                && diff.getValue() == Statement.EMPTY_STATEMENT;
    }
}