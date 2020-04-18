package me.khudyakov.staticanalyzer.entity;

import me.khudyakov.staticanalyzer.entity.syntaxtree.SyntaxTree;

public class Program {

    private final ProgramCode programCode;
    private final SyntaxTree syntaxTree;

    public void execute() {
        syntaxTree.getRoot().execute();
    }

    public Program(ProgramCode programCode, SyntaxTree syntaxTree) {
        this.programCode = programCode;
        this.syntaxTree = syntaxTree;
    }

    // this method may be used in future
    public ProgramCode getProgramCode() {
        return programCode;
    }

    public SyntaxTree getSyntaxTree() {
        return syntaxTree;
    }
}
