package me.khudyakov.semanticanalyzer;

import me.khudyakov.semanticanalyzer.editor.EditorGUI;
import me.khudyakov.semanticanalyzer.editor.OutputAreaWriter;
import me.khudyakov.semanticanalyzer.program.Program;
import me.khudyakov.semanticanalyzer.program.ProgramCode;
import me.khudyakov.semanticanalyzer.program.SemanticTree;
import me.khudyakov.semanticanalyzer.service.CodeParser;
import me.khudyakov.semanticanalyzer.service.CodeParserImpl;
import me.khudyakov.semanticanalyzer.service.StaticAnalyzer;
import me.khudyakov.semanticanalyzer.service.StaticAnalyzerImpl;
import me.khudyakov.semanticanalyzer.util.ExpressionConverterException;
import me.khudyakov.semanticanalyzer.util.ExpressionExecutionException;
import me.khudyakov.semanticanalyzer.util.StaticAnalyzerException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProgramCodeExecutorTest {

    private final StaticAnalyzer staticAnalyzer = new StaticAnalyzerImpl();
    private final CodeParser codeParser = new CodeParserImpl();

    private static final EditorGUI editorGUI = new EditorGUI();

    @Test
    void execute() throws IOException, ParseException, ExpressionConverterException, StaticAnalyzerException, ExpressionExecutionException {
        OutputAreaWriter.clear();
        String inputProgram = "@x  = 10;\n" +
                "@second = 20;\n" +
                "if (second - 19) {\n" +
                "  x + 1;\n" +
                "}\n" +
                "x*second + second/x*3;";

        ProgramCode programCode = codeParser.parse(inputProgram);
        SemanticTree semanticTree = staticAnalyzer.analyze(programCode);
        Program program = new Program(programCode, semanticTree);

        program.execute();

        String[] nums = OutputAreaWriter.getText().split("\n");
        assertEquals(11, Integer.parseInt(nums[0]));
        assertEquals(206, Integer.parseInt(nums[1]));

        OutputAreaWriter.clear();
    }

    @Test
    void execute2() throws IOException, ParseException, ExpressionConverterException, StaticAnalyzerException, ExpressionExecutionException {
        OutputAreaWriter.clear();
        String inputProgram = "{ @ x= 2;\n" +
                " @second = -3 ; }\n" +
                "if (second - 19 > -23) {\n" +
                "  x + 1;\n" +
                " @  x = x - 1 ;" +
                "}\n" +
                "{ { x*second + second/x*3; } " +
                "-x*second ; }";

        ProgramCode programCode = codeParser.parse(inputProgram);
        SemanticTree semanticTree = staticAnalyzer.analyze(programCode);
        Program program = new Program(programCode, semanticTree);

        program.execute();

        String[] nums = OutputAreaWriter.getText().split("\n");
        assertEquals(3, Integer.parseInt(nums[0]));
        assertEquals(-12, Integer.parseInt(nums[1]));
        assertEquals(3, Integer.parseInt(nums[2]));

        OutputAreaWriter.clear();
    }
}