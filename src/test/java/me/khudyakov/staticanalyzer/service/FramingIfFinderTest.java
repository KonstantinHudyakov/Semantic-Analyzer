package me.khudyakov.staticanalyzer.service;

import me.khudyakov.staticanalyzer.util.SyntaxAnalyzerException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.ParseException;

import static me.khudyakov.staticanalyzer.service.ServiceUtils.*;
import static org.junit.jupiter.api.Assertions.*;

class FramingIfFinderTest {

    private SyntaxTreeCache cache;

    @BeforeEach
    private void tearUp() {
        cache = new SyntaxTreeCache(3);
    }

    @Test
    void catchFramingIf() throws SyntaxAnalyzerException, ParseException {
        String text1 = "3 * 7;";
        String text2 = "if() 3 * 7;";
        String text3 = "if() { 3 * 7; }";
        String text4 = "if(3 > 2) if() { 3 * 7; }";
        String text5 = "if(3 > 2) { if() { 3 * 7; } }";

        addChangeSequence(cache, text1, text2, text3);
        assertTrue(framingIfFinder.featureFound(cache));

        addChangeSequence(cache, text4, text5);
        assertTrue(framingIfFinder.featureFound(cache));
    }

    @Test
    void catchFramingIfInBlock() throws SyntaxAnalyzerException, ParseException {
        String text1 = "1; if(3 > 2) { @x = 3; 3 * 7; }";
        String text2 = "1; if(3 > 2) { if(1) @x = 3; 3 * 7; }";
        String text3 = "1; if(3 > 2) { if(1) { @x = 3; 3 * 7; } }";

        addChangeSequence(cache, text1, text2, text3);
        assertTrue(framingIfFinder.featureFound(cache));
    }

    @Test
    void catchAfterRemoving() throws SyntaxAnalyzerException, ParseException {
        String text1 = "if() { 3 * 7; }";
        String text2 = "if() 3 * 7;";
        String text3 = "3 * 7;";

        addChangeSequence(cache, text1, text2, text3, text2, text1);
        assertTrue(framingIfFinder.featureFound(cache));
    }

    @Test
    void catchWithMisprint() throws SyntaxAnalyzerException, ParseException {
        String text1 = "3 * 7; @x = 44;";
        String text2 = "if() 3 * 7; @x = 44;";
        String text3 = "if() [ 3 * 7; @x = 44;";
        String text4 = "if() 3 * 7; @x = 44;";
        String text5 = "if() 3 * 7; @x = 44; }";
        String text6 = "if(){3 * 7; @x = 44; }";

        addChangeSequence(cache, text1, text2);
        assertThrows(ParseException.class, () -> addChange(cache, text3));
        addChange(cache, text4);
        assertThrows(SyntaxAnalyzerException.class, () -> addChange(cache, text5));
        addChange(cache, text6);

        assertTrue(framingIfFinder.featureFound(cache));
    }

    @Test
    void catchFramingBlockAndStatement() throws SyntaxAnalyzerException, ParseException {
        String text1 = "@x = 5; {4 * 7; } @y = 3;";
        String text2 = "@x = 5; if() {4 * 7; } @y = 3;";
        String text3 = "@x = 5; if() { {4 * 7; } @y = 3; }";

        addChangeSequence(cache, text1, text2, text3);
        assertTrue(framingIfFinder.featureFound(cache));
    }

    @Test
    void dontCatchInsertBlock() throws SyntaxAnalyzerException, ParseException {
        String text1 = "if() 3 * 7;";
        String text2 = "if() { 3 * 7; }";

        addChangeSequence(cache, text1, text2);
        assertFalse(framingIfFinder.featureFound(cache));
    }

    @Test
    void dontCatchEditingAfterIf() throws SyntaxAnalyzerException, ParseException {
        String text1 = "@x = 35; 3 * 7;";
        String text2 = "@x = 35; if() 3 * 7;";
        String text3 = "@x = 35; if() 3 * 7 + 4;";
        String text4 = "@x = 35; if(){ 3 * 7 + 4;}";

        addChangeSequence(cache, text1, text2, text3, text4);
        assertFalse(framingIfFinder.featureFound(cache));
    }

    @Test
    void dontCatchInsertIfToBlock() throws SyntaxAnalyzerException, ParseException {
        String text1 = "@x = 3; { x * 5; }";
        String text2 = "@x = 3; if(1) { x * 5; }";

        addChangeSequence(cache, text1, text2);
        assertFalse(framingIfFinder.featureFound(cache));
    }

    @Test
    void dontCatchFramingNothing() throws SyntaxAnalyzerException, ParseException {
        String text1 = "3 * 7;";
        String text2 = "if() 3 * 7;";
        String text3 = "if() {} 3 * 7;";

        addChangeSequence(cache, text1, text2, text3);
        assertFalse(framingIfFinder.featureFound(cache));
    }

    @Test
    void dontCatchFramingEmptyBlock() throws SyntaxAnalyzerException, ParseException {
        String text1 = "@y = 3; { } (y - 5) * 3;";
        String text2 = "@y = 3; if(abc) { } (y - 5) * 3;";
        String text3 = "@y = 3; if(abc) { { } } (y - 5) * 3;";

        addChangeSequence(cache, text1, text2, text3);
        assertFalse(framingIfFinder.featureFound(cache));
    }

    @Test
    void dontCatchFramingFullBlock() throws SyntaxAnalyzerException, ParseException {
        String text1 = "{ @x = 33; 3 * x; @y = x + 10; }";
        String text2 = "if() { @x = 33; 3 * x; @y = x + 10; }";
        String text3 = "if() { { @x = 33; 3 * x; @y = x + 10; } }";

        addChangeSequence(cache, text1, text2, text3);
        assertFalse(framingIfFinder.featureFound(cache));
    }

    @Test
    void dontCatchInsertingEmptyBlock() throws SyntaxAnalyzerException, ParseException {
        String text1 = "@x = 3; { 3 * 3; 2 - 1; }";
        String text2 = "@x = 3; if() { 3 * 3; 2 - 1; }";
        String text3 = "@x = 3; if() {{} 3 * 3; 2 - 1; }";

        addChangeSequence(cache, text1, text2, text3);
        assertFalse(framingIfFinder.featureFound(cache));
    }

    @Test
    void dontCatchInsertingBlockToBlock() throws SyntaxAnalyzerException, ParseException {
        String text1 = "@x = 3; { 3 * 3; 2 - 1; }";
        String text2 = "@x = 3; if() { 3 * 3; 2 - 1; }";
        String text3 = "@x = 3; if() {{ 3 * 3; } 2 - 1; }";

        addChangeSequence(cache, text1, text2, text3);
        assertFalse(framingIfFinder.featureFound(cache));
    }
}