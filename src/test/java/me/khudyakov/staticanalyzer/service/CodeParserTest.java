package me.khudyakov.staticanalyzer.service;

import me.khudyakov.staticanalyzer.entity.ProgramCode;
import org.junit.jupiter.api.Test;

import java.text.ParseException;

import static me.khudyakov.staticanalyzer.service.ServiceUtils.codeParser;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CodeParserTest {

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
                "x*second ; }";
        String expectedResult = "[{, @, x, =, 2, ;, @, second, =, -3, }, ;, if, (, second, -, 19, ), {, x, +, 1, ;, @, x, =, x, -, 1, ;, }, {, {, x, *, second, +, second, /, x, *, 3, ;, }, x, *, second, ;, }]";

        ProgramCode result = codeParser.parse(inputProgram);
        assertEquals(expectedResult, result.toString());
    }

    @Test
    void parseComments() throws ParseException {
        String inputProgram = "@x = 3; //  sdfdf 2342424\n" +
                "//5 * 2 - 3;\n" +
                "3 * 2;//\n" +
                "{ @y = 6 + x;//@zer= y/x;\n" +
                "}\n" +
                "//end program comment";
        String expectedResult = "[@, x, =, 3, ;, //  sdfdf 2342424, //5 * 2 - 3;, 3, *, 2, ;, //, {, @, y, =, 6, +, x, ;, //@zer= y/x;, }, //end program comment]";

        ProgramCode result = codeParser.parse(inputProgram);
        assertEquals(expectedResult, result.toString());
    }
}