package me.khudyakov.staticanalyzer.editor;

import me.khudyakov.staticanalyzer.entity.ProgramCode;
import me.khudyakov.staticanalyzer.entity.syntaxtree.SyntaxTree;
import me.khudyakov.staticanalyzer.service.CodeParser;
import me.khudyakov.staticanalyzer.service.FeatureFinder;
import me.khudyakov.staticanalyzer.service.SyntaxAnalyzer;
import me.khudyakov.staticanalyzer.service.SyntaxTreeCache;

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
    private final SyntaxTreeCache syntaxTreeCache;
    private final FeatureFinder framingIfFinder;

    public CodeChangedListener(EditorGUI editorGUI, CodeParser codeParser, SyntaxAnalyzer syntaxAnalyzer,
                               SyntaxTreeCache syntaxTreeCache,
                               FeatureFinder framingIfFinder) {
        this.editorGUI = editorGUI;
        this.codeParser = codeParser;
        this.syntaxAnalyzer = syntaxAnalyzer;
        this.syntaxTreeCache = syntaxTreeCache;
        this.framingIfFinder = framingIfFinder;
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        try {
            SyntaxTree syntaxTree = parseAndAnalyzeProgram();
            boolean isChanged = syntaxTreeCache.addNewSyntaxTreeVersion(syntaxTree);
            if(isChanged && framingIfFinder.featureFound(syntaxTreeCache)) {
                showFramingIfPopup();
            }
        } catch (Exception ex) {
            // do nothing
        }
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        try {
            SyntaxTree syntaxTree = parseAndAnalyzeProgram();
            syntaxTreeCache.addNewSyntaxTreeVersion(syntaxTree);
        } catch (Exception ex) {
            // do nothing
        }
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        // do nothing
    }

    private SyntaxTree parseAndAnalyzeProgram() throws Exception {
        String text = getText();
        ProgramCode programCode = codeParser.parse(text);
        return syntaxAnalyzer.createSyntaxTree(programCode);
    }

    private void showFramingIfPopup() {
        JTextArea codeArea = editorGUI.getCodeArea();
        Caret caret = codeArea.getCaret();
        Point caretPos = caret.getMagicCaretPosition();
        Point codeAreaPos = codeArea.getLocationOnScreen();
        editorGUI.showPopupLabel("Added framing if statement!!!", caretPos.x + codeAreaPos.x + 15, caretPos.y + codeAreaPos.y + 15);
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
