package me.khudyakov.staticanalyzer.entity.syntaxtree.statement;

public abstract class Statement {

    private final int startInd;
    private final int endInd;

    public Statement(int startInd, int endInd) {
        this.startInd = startInd;
        this.endInd = endInd;
    }

    public abstract void execute();

    public int getStartInd() {
        return startInd;
    }

    public int getEndInd() {
        return endInd;
    }
}
