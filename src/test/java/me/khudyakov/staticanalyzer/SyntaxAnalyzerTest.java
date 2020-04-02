package me.khudyakov.staticanalyzer;

import me.khudyakov.staticanalyzer.program.ProgramCode;
import me.khudyakov.staticanalyzer.program.SyntaxTree;
import me.khudyakov.staticanalyzer.service.CodeParser;
import me.khudyakov.staticanalyzer.service.CodeParserImpl;
import me.khudyakov.staticanalyzer.service.SyntaxAnalyzer;
import me.khudyakov.staticanalyzer.service.SyntaxAnalyzerImpl;
import me.khudyakov.staticanalyzer.util.ExpressionConverterException;
import me.khudyakov.staticanalyzer.util.ExpressionExecutionException;
import me.khudyakov.staticanalyzer.util.SyntaxAnalyzerException;
import org.junit.jupiter.api.Test;

import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class SyntaxAnalyzerTest {

    private final SyntaxAnalyzer syntaxAnalyzer = new SyntaxAnalyzerImpl();
    private final CodeParser codeParser = new CodeParserImpl();

    @Test
    void analyze() throws SyntaxAnalyzerException, ExpressionConverterException, ParseException, ExpressionExecutionException {
        String inputProgram = "@x  = 10;\n" +
                "@second = 20;\n" +
                "if (second - 19) {\n" +
                "  x + 1;\n" +
                "}\n" +
                "x*second + second/x*3;";
        ProgramCode programCode = codeParser.parse(inputProgram);

        SyntaxTree syntaxTree = syntaxAnalyzer.analyze(programCode);

        assertNotNull(syntaxTree);
    }

    @Test
    void analyze2() throws SyntaxAnalyzerException, ExpressionConverterException, ParseException, ExpressionExecutionException {
        String inputProgram = "{ @ x= 2;\n" +
                " @second = -3 ; }\n" +
                "if (second - 19) {\n" +
                "  x + 1;\n" +
                " @  x = x - 1 ;" +
                "}\n" +
                "{ { x*second + second/x*3; } " +
                "-x*second ; }";
        ProgramCode programCode = codeParser.parse(inputProgram);

        SyntaxTree syntaxTree = syntaxAnalyzer.analyze(programCode);

        assertNotNull(syntaxTree);
    }
}