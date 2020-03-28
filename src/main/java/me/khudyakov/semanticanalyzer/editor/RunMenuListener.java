package me.khudyakov.semanticanalyzer.editor;

import me.khudyakov.semanticanalyzer.program.Program;
import me.khudyakov.semanticanalyzer.program.ProgramCode;
import me.khudyakov.semanticanalyzer.program.SemanticTree;
import me.khudyakov.semanticanalyzer.service.CodeParser;
import me.khudyakov.semanticanalyzer.service.CodeParserImpl;
import me.khudyakov.semanticanalyzer.service.StaticAnalyzer;
import me.khudyakov.semanticanalyzer.service.StaticAnalyzerImpl;

import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

public class RunMenuListener implements MenuListener {

    private final Document codeDocument;

    private final CodeParser codeParser = new CodeParserImpl();
    private final StaticAnalyzer staticAnalyzer = new StaticAnalyzerImpl();

    public RunMenuListener(Document codeDocument) {
        this.codeDocument = codeDocument;
    }

    @Override
    public void menuSelected(MenuEvent e) {
        OutputAreaWriter.clear();
        String text = getText();
        try {
            Program program = new Program();
            ProgramCode programCode = codeParser.parse(text);
            program.setProgramCode(programCode);
            SemanticTree semanticTree = staticAnalyzer.analyze(programCode);
            program.setSemanticTree(semanticTree);

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
