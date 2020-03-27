package me.khudyakov.semanticanalyzer.editor;

import me.khudyakov.semanticanalyzer.program.Program;
import me.khudyakov.semanticanalyzer.program.ProgramCode;
import me.khudyakov.semanticanalyzer.program.SemanticTree;
import me.khudyakov.semanticanalyzer.service.CodeParser;
import me.khudyakov.semanticanalyzer.service.CodeParserImpl;
import me.khudyakov.semanticanalyzer.service.StaticAnalyzer;
import me.khudyakov.semanticanalyzer.service.StaticAnalyzerImpl;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

public class CodeChangedListener implements DocumentListener {

    private final Document document;

    private final CodeParser codeParser = new CodeParserImpl();
    private final StaticAnalyzer staticAnalyzer = new StaticAnalyzerImpl();


    public CodeChangedListener(Document document) {
        this.document = document;
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        handleTextChanged();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        handleTextChanged();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        handleTextChanged();
    }

    private void handleTextChanged() {
        String text = getText();
        Program program = new Program();
        try {
            ProgramCode programCode = codeParser.parse(text);
            program.setProgramCode(programCode);
            SemanticTree semanticTree = staticAnalyzer.analyze(programCode);
            //System.out.println("ok");
        } catch (Exception e) {

        }
    }

    private String getText() {
        String text = "";
        try {
            text = document.getText(0, document.getLength());
        } catch (BadLocationException e) {
            // never happens
        }
        return text;
    }
}
