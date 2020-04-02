package me.khudyakov.semanticanalyzer.editor;

import me.khudyakov.semanticanalyzer.components.syntaxtree.StatementListNode;
import me.khudyakov.semanticanalyzer.program.Program;
import me.khudyakov.semanticanalyzer.program.ProgramCode;
import me.khudyakov.semanticanalyzer.program.SyntaxTree;
import me.khudyakov.semanticanalyzer.service.*;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.Document;
import java.awt.*;
import java.util.Collections;

public class CodeChangedListener implements DocumentListener {

    private final EditorGUI editorGUI;

    private final CodeParser codeParser = new CodeParserImpl();
    private final SyntaxAnalyzer syntaxAnalyzer = new SyntaxAnalyzerImpl();
    private final FeatureFinder framingIfFinder = new FramingIfFeatureFinder();

    // version of program before update event
    private Program oldVersion;


    public CodeChangedListener(EditorGUI editorGUI) {
        this.editorGUI = editorGUI;
        ProgramCode programCode = new ProgramCode(Collections.emptyList());
        SyntaxTree syntaxTree = new SyntaxTree(new StatementListNode());
        oldVersion = new Program(programCode, syntaxTree);
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        try {
            Program curVersion = parseAndAnalyzeProgram();
            if(framingIfFinder.featureFound(oldVersion, curVersion)) {
                JTextArea codeArea = editorGUI.getCodeArea();
                Caret caret = codeArea.getCaret();
                Point caretPos = caret.getMagicCaretPosition();
                Point codeAreaPos = codeArea.getLocationOnScreen();
                editorGUI.showPopupLabel("Added framing if statement!!!", caretPos.x + codeAreaPos.x + 15, caretPos.y + codeAreaPos.y + 15);
            }
            oldVersion = curVersion;
        } catch (Exception ex) {
            // do nothing
        }
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        try {
            oldVersion = parseAndAnalyzeProgram();
        } catch (Exception ex) {
            // do nothing
        }
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        // do nothing
    }

    private Program parseAndAnalyzeProgram() throws Exception {
        String text = getText();
        ProgramCode programCode = codeParser.parse(text);
        SyntaxTree syntaxTree = syntaxAnalyzer.analyze(programCode);

        return new Program(programCode, syntaxTree);
    }

    private String getText() {
        String text = "";
        Document document = editorGUI.getCodeArea().getDocument();
        try {
            text = document.getText(0, document.getLength());
        } catch (BadLocationException e) {
            // never happens
        }
        return text;
    }
}
