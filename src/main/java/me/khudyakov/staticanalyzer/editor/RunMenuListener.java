package me.khudyakov.staticanalyzer.editor;

import me.khudyakov.staticanalyzer.entity.Program;
import me.khudyakov.staticanalyzer.entity.ProgramCode;
import me.khudyakov.staticanalyzer.entity.syntaxtree.SyntaxTree;
import me.khudyakov.staticanalyzer.service.CodeParser;
import me.khudyakov.staticanalyzer.service.SyntaxAnalyzer;

import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

public class RunMenuListener implements MenuListener {

    private final Document codeDocument;

    private final CodeParser codeParser;
    private final SyntaxAnalyzer syntaxAnalyzer;

    public RunMenuListener(Document codeDocument, CodeParser codeParser, SyntaxAnalyzer syntaxAnalyzer) {
        this.codeDocument = codeDocument;
        this.codeParser = codeParser;
        this.syntaxAnalyzer = syntaxAnalyzer;
    }

    @Override
    public void menuSelected(MenuEvent e) {
        OutputAreaWriter.clear();
        String text = getText();
        try {
            ProgramCode programCode = codeParser.parse(text);
            SyntaxTree syntaxTree = syntaxAnalyzer.createSyntaxTree(programCode);
            Program program = new Program(programCode, syntaxTree);

            program.execute();
        } catch (Exception ex) {
            OutputAreaWriter.setContent(ex.getMessage());
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
