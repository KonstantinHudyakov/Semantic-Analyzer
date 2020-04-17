package me.khudyakov.staticanalyzer.service;

import me.khudyakov.staticanalyzer.util.ExpressionConverterException;
import me.khudyakov.staticanalyzer.util.ExpressionExecutionException;
import me.khudyakov.staticanalyzer.util.SyntaxAnalyzerException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.ParseException;

import static me.khudyakov.staticanalyzer.service.ServiceUtils.*;
import static org.junit.jupiter.api.Assertions.*;

class FramingIfFeatureFinderTest {

    private SyntaxTreeChangesCache cache;

    @BeforeEach
    private void tearUp() {
        cache = new SyntaxTreeChangesCache(3);
    }

    @Test
    void catchFramingIf() throws ExpressionConverterException, ExpressionExecutionException, SyntaxAnalyzerException, ParseException {
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
    void catchAfterRemoving() throws ExpressionConverterException, ExpressionExecutionException, SyntaxAnalyzerException, ParseException {
        String text1 = "if() { 3 * 7; }";
        String text2 = "if() 3 * 7;";
        String text3 = "3 * 7;";

        addChangeSequence(cache, text1, text2, text3, text2, text1);
        assertTrue(framingIfFinder.featureFound(cache));
    }

    @Test
    void catchWithMisprint() throws ExpressionConverterException, ExpressionExecutionException, SyntaxAnalyzerException, ParseException {
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
    void dontCatchEmptyBlock() throws ExpressionConverterException, ExpressionExecutionException, SyntaxAnalyzerException, ParseException {
        String text1 = "3 * 7;";
        String text2 = "if() 3 * 7;";
        String text3 = "if() {} 3 * 7;";

        addChangeSequence(cache, text1, text2, text3);
        assertFalse(framingIfFinder.featureFound(cache));
    }

    @Test
    void dontCatchInsertBlock() throws ExpressionConverterException, ExpressionExecutionException, SyntaxAnalyzerException, ParseException {
        String text1 = "if() 3 * 7;";
        String text2 = "if() { 3 * 7; }";

        addChangeSequence(cache, text1, text2);
        assertFalse(framingIfFinder.featureFound(cache));
    }

    @Test
    void dontCatchEditingAfterIf() throws ExpressionConverterException, ExpressionExecutionException, SyntaxAnalyzerException, ParseException {
        String text1 = "@x = 35; 3 * 7;";
        String text2 = "@x = 35; if() 3 * 7;";
        String text3 = "@x = 35; if() 3 * 7 + 4;";
        String text4 = "@x = 35; if(){ 3 * 7 + 4;}";

        addChangeSequence(cache, text1, text2, text3, text4);
        assertFalse(framingIfFinder.featureFound(cache));
    }

}