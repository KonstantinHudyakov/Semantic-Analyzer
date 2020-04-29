package me.khudyakov.staticanalyzer.service;

import me.khudyakov.staticanalyzer.editor.EditorGUI;
import me.khudyakov.staticanalyzer.editor.OutputAreaWriter;
import me.khudyakov.staticanalyzer.entity.syntaxtree.SyntaxTree;
import me.khudyakov.staticanalyzer.util.SyntaxAnalyzerException;
import org.junit.jupiter.api.Test;

import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProgramExecutionTest {

    private static final SyntaxTreeVisitor programExecutionVisitor = new ProgramExecutionVisitor();

    private static final EditorGUI editorGUI = new EditorGUI();

    @Test
    void execute() throws ParseException, SyntaxAnalyzerException {
        OutputAreaWriter.clear();
        String inputProgram = "@x  = 10;\n" +
                "@second = 20;\n" +
                "if (second - 19) {\n" +
                "  x + 1;\n" +
                "}\n" +
                "x*second + second/x*3;";

        SyntaxTree syntaxTree = ServiceUtils.parseAndAnalyze(inputProgram);
        syntaxTree.accept(programExecutionVisitor);

        String[] nums = OutputAreaWriter.getText().split("\n");
        assertEquals(11, Integer.parseInt(nums[0]));
        assertEquals(206, Integer.parseInt(nums[1]));

        OutputAreaWriter.clear();
    }

    @Test
    void execute2() throws ParseException, SyntaxAnalyzerException {
        OutputAreaWriter.clear();
        String inputProgram = "{ @ x= 2;\n" +
                " @second = -3 ; }\n" +
                "if (second - 19 > -23) {\n" +
                "  x + 1;\n" +
                " @  x = x - 1 ;" +
                "}\n" +
                "{ { x*second + second/x*3; } " +
                "x*second ; }";

        SyntaxTree syntaxTree = ServiceUtils.parseAndAnalyze(inputProgram);
        syntaxTree.accept(programExecutionVisitor);

        String[] nums = OutputAreaWriter.getText().split("\n");
        assertEquals(3, Integer.parseInt(nums[0]));
        assertEquals(-12, Integer.parseInt(nums[1]));
        assertEquals(-3, Integer.parseInt(nums[2]));

        OutputAreaWriter.clear();
    }
}