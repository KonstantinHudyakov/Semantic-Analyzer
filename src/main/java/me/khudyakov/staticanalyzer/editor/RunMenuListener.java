package me.khudyakov.staticanalyzer.editor;

import me.khudyakov.staticanalyzer.program.Program;
import me.khudyakov.staticanalyzer.program.ProgramCode;
import me.khudyakov.staticanalyzer.program.SyntaxTree;
import me.khudyakov.staticanalyzer.service.CodeParser;
import me.khudyakov.staticanalyzer.service.CodeParserImpl;
import me.khudyakov.staticanalyzer.service.SyntaxAnalyzer;
import me.khudyakov.staticanalyzer.service.SyntaxAnalyzerImpl;

import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

public class RunMenuListener implements MenuListener {

    private final Document codeDocument;

    private final CodeParser codeParser = new CodeParserImpl();
    private final SyntaxAnalyzer syntaxAnalyzer = new SyntaxAnalyzerImpl();

    public RunMenuListener(Document codeDocument) {
        this.codeDocument = codeDocument;
    }

    @Override
    public void menuSelected(MenuEvent e) {
        OutputAreaWriter.clear();
        String text = getText();
        try {
            ProgramCode programCode = codeParser.parse(text);
            SyntaxTree syntaxTree = syntaxAnalyzer.analyze(programCode);
            Program program = new Program(programCode, syntaxTree);

            program.execute();
        } catch (Exception ex) {
            OutputAreaWriter.setContent("Errors in the code!");
        }
    }

    @Override
    public void menuDeselected(MenuEvent e) {
        // do nothing
    }

    @Override
    public void menuCanceled(MenuEvent e) {
        // do nothing
    }

    private String getText() {
        String text = "";
        try {
            text = codeDocument.getText(0, codeDocument.getLength());
        } catch (BadLocationException e) {
            // never happens
        }
        return text;
    }
}
