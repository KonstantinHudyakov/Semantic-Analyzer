package me.khudyakov.semanticanalyzer.program;

import me.khudyakov.semanticanalyzer.util.ExpressionExecutionException;

public class Program {

    private ProgramCode programCode;
    private SemanticTree semanticTree;

    public Program() {
    }

    public void execute() throws ExpressionExecutionException {
        semanticTree.getRoot().executeSubtree();
    }

    public Program(ProgramCode programCode, SemanticTree semanticTree) {
        this.programCode = programCode;
        this.semanticTree = semanticTree;
    }

    public ProgramCode getProgramCode() {
        return programCode;
    }

    public void setProgramCode(ProgramCode programCode) {
        this.programCode = programCode;
    }

    public SemanticTree getSemanticTree() {
        return semanticTree;
    }

    public void setSemanticTree(SemanticTree semanticTree) {
        this.semanticTree = semanticTree;
    }
}
