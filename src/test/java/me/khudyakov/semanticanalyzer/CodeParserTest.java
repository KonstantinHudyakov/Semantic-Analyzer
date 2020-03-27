package me.khudyakov.semanticanalyzer;

import me.khudyakov.semanticanalyzer.program.ProgramCode;
import me.khudyakov.semanticanalyzer.service.CodeParser;
import me.khudyakov.semanticanalyzer.service.CodeParserImpl;
import org.junit.jupiter.api.Test;

import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CodeParserTest {

    private final CodeParser codeParser = new CodeParserImpl();

    @Test
    void parse() throws ParseException {
        String inputProgram = "@x  = 10;\n" +
                "@second = 20;\n" +
                "if (second - 19) {\n" +
                "  x + 1;\n" +
                "}\n" +
                "x*second + second/x*3;";
        String expectedResult = "[@, x, =, 10, ;, @, second, =, 20, ;, if, (, second, -, 19, ), {, x, +, 1, ;, }, x, *, second, +, second, /, x, *, 3, ;]";

        ProgramCode result = codeParser.parse(inputProgram);
        assertEquals(expectedResult, result.toString());
    }

    @Test
    void parse2() throws ParseException {
        String inputProgram = "{ @ x= 2;\n" +
                " @second = -3 };\n" +
                "if (second - 19) {\n" +
                "  x + 1;\n" +
                " @  x = x - 1 ;" +
                "}\n" +
                "{ { x*second + second/x*3; } " +
                "-x*second ; }";
        String expectedResult = "[{, @, x, =, 2, ;, @, second, =, -, 3, }, ;, if, (, second, -, 19, ), {, x, +, 1, ;, @, x, =, x, -, 1, ;, }, {, {, x, *, second, +, second, /, x, *, 3, ;, }, -, x, *, second, ;, }]";

        ProgramCode result = codeParser.parse(inputProgram);
        assertEquals(expectedResult, result.toString());
    }
}