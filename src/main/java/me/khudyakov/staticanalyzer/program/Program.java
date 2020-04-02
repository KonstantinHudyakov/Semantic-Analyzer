package me.khudyakov.staticanalyzer.program;

import me.khudyakov.staticanalyzer.util.ExpressionExecutionException;

public class Program {

    private ProgramCode programCode;
    private SyntaxTree syntaxTree;

    public Program() {
    }

    public void execute() throws ExpressionExecutionException {
        syntaxTree.getRoot().executeSubtree();
    }

    public Program(ProgramCode programCode, SyntaxTree syntaxTree) {
        this.programCode = programCode;
        this.syntaxTree = syntaxTree;
    }

    public ProgramCode getProgramCode() {
        return programCode;
    }

    public void setProgramCode(ProgramCode programCode) {
        this.programCode = programCode;
    }

    public SyntaxTree getSyntaxTree() {
        return syntaxTree;
    }

    public void setSyntaxTree(SyntaxTree syntaxTree) {
        this.syntaxTree = syntaxTree;
    }
}
