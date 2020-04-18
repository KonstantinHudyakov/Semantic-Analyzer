package me.khudyakov.staticanalyzer.service;

import me.khudyakov.staticanalyzer.entity.syntaxtree.SyntaxTree;
import me.khudyakov.staticanalyzer.util.SyntaxAnalyzerException;
import org.junit.jupiter.api.Test;

import java.text.ParseException;

import static me.khudyakov.staticanalyzer.service.ServiceUtils.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SyntaxAnalyzerTest {

    @Test
    void analyze() throws SyntaxAnalyzerException, ParseException {
        String inputProgram = "@x  = 10;\n" +
                "@second = 20;\n" +
                "if (second - 19) {\n" +
                "  x + 1;\n" +
                "}\n" +
                "x*second + second/x*3;";

        SyntaxTree syntaxTree = parseAndAnalyze(inputProgram);
        assertNotNull(syntaxTree);
    }

    @Test
    void analyze2() throws SyntaxAnalyzerException,  ParseException {
        String inputProgram = "{ @ x= 2;\n" +
                " @second = -3 ; }\n" +
                "if (second - 19) {\n" +
                "  x + 1;\n" +
                " @  x = x - 1 ;" +
                "}\n" +
                "{ { x*second + second/x*3; } " +
                "x*second ; }";

        SyntaxTree syntaxTree = parseAndAnalyze(inputProgram);
        assertNotNull(syntaxTree);
    }

    @Test
    void assignInput() throws SyntaxAnalyzerException, ParseException {
        String text1 = "@";
        String text2 = "@x";
        String text3 = "@x=";
        String text4 = "@x=2";
        String text5 = "@x=25";
        String text6 = "@x=25;";

        analyzeAndCatchExceptions(SyntaxAnalyzerException.class, text1, text2, text3, text4, text5);
        assertNotNull(parseAndAnalyze(text6));
    }

    @Test
    void testModifying() throws SyntaxAnalyzerException, ParseException {
        String text1 = "@x = 3; if(x > 1) { x * 3; }";
        String text2 = "@x = 3; if(x > 1) { x + 3; }";
        String text3 = "@x = 3; if(x > 1) x + 3;";
        String text4 = "if(7 * 4 > 27) @x = 3; if(x > 1) x + 3;";
        String text5 = "if(7 * 4 > 27) { @x = 3; if(x > 1) x + 3; }";
        String text6 = "if(7 * 4 > 27) { @var = 3; if(x > 1) x + 3; }";

        analyzeOrThrow(text1, text2, text3, text4, text5, text6);
    }
}