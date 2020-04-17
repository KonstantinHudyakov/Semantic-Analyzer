package me.khudyakov.staticanalyzer.editor;

import me.khudyakov.staticanalyzer.program.Program;
import me.khudyakov.staticanalyzer.program.ProgramCode;
import me.khudyakov.staticanalyzer.program.SyntaxTree;
import me.khudyakov.staticanalyzer.service.CodeParser;
import me.khudyakov.staticanalyzer.service.FeatureFinder;
import me.khudyakov.staticanalyzer.service.SyntaxAnalyzer;
import me.khudyakov.staticanalyzer.service.SyntaxTreeChangesCache;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.Document;
import java.awt.*;

public class CodeChangedListener implements DocumentListener {

    private final EditorGUI editorGUI;

    private final CodeParser codeParser;
    private final SyntaxAnalyzer syntaxAnalyzer;
    private final SyntaxTreeChangesCache syntaxTreeChangesCache;
    private final FeatureFinder framingIfFinder;

    public CodeChangedListener(EditorGUI editorGUI, CodeParser codeParser, SyntaxAnalyzer syntaxAnalyzer,
                               SyntaxTreeChangesCache syntaxTreeChangesCache,
                               FeatureFinder framingIfFinder) {
        this.editorGUI = editorGUI;
        this.codeParser = codeParser;
        this.syntaxAnalyzer = syntaxAnalyzer;
        this.syntaxTreeChangesCache = syntaxTreeChangesCache;
        this.framingIfFinder = framingIfFinder;
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        try {
            Program program = parseAndAnalyzeProgram();
            boolean isChanged = syntaxTreeChangesCache.addNewChange(program.getSyntaxTree());
            if(isChanged && framingIfFinder.featureFound(syntaxTreeChangesCache)) {
                JTextArea codeArea = editorGUI.getCodeArea();
                Caret caret = codeArea.getCaret();
                Point caretPos = caret.getMagicCaretPosition();
                Point codeAreaPos = codeArea.getLocationOnScreen();
                editorGUI.showPopupLabel("Added framing if statement!!!", caretPos.x + codeAreaPos.x + 15, caretPos.y + codeAreaPos.y + 15);
            }
        } catch (Exception ex) {
            // do nothing
        }
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        try {
            Program program = parseAndAnalyzeProgram();
            syntaxTreeChangesCache.addNewChange(program.getSyntaxTree());
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
