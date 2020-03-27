package me.khudyakov.semanticanalyzer;

import me.khudyakov.semanticanalyzer.program.ProgramCode;
import me.khudyakov.semanticanalyzer.program.SemanticTree;
import me.khudyakov.semanticanalyzer.service.CodeParser;
import me.khudyakov.semanticanalyzer.service.CodeParserImpl;
import me.khudyakov.semanticanalyzer.service.StaticAnalyzer;
import me.khudyakov.semanticanalyzer.service.StaticAnalyzerImpl;
import me.khudyakov.semanticanalyzer.util.ExpressionConverterException;
import me.khudyakov.semanticanalyzer.util.StaticAnalyzerException;
import org.junit.jupiter.api.Test;

import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class StaticAnalyzerTest {

    private final StaticAnalyzer staticAnalyzer = new StaticAnalyzerImpl();
    private final CodeParser codeParser = new CodeParserImpl();

    @Test
    void analyze() throws StaticAnalyzerException, ExpressionConverterException, ParseException {
        String inputProgram = "@x  = 10;\n" +
                "@second = 20;\n" +
                "if (second - 19) {\n" +
                "  x + 1;\n" +
                "}\n" +
                "x*second + second/x*3;";
        ProgramCode programCode = codeParser.parse(inputProgram);

        SemanticTree semanticTree = staticAnalyzer.analyze(programCode);

        assertNotNull(semanticTree);
    }

    @Test
    void analyze2() throws StaticAnalyzerException, ExpressionConverterException, ParseException {
        String inputProgram = "{ @ x= 2;\n" +
                " @second = -3 ; }\n" +
                "if (second - 19) {\n" +
                "  x + 1;\n" +
                " @  x = x - 1 ;" +
                "}\n" +
                "{ { x*second + second/x*3; } " +
                "-x*second ; }";
        ProgramCode programCode = codeParser.parse(inputProgram);

        SemanticTree semanticTree = staticAnalyzer.analyze(programCode);

        assertNotNull(semanticTree);
    }
}