package me.khudyakov.staticanalyzer.entity.syntaxtree.statement;

import java.util.Collections;
import java.util.List;

public abstract class Statement {

    public static final Statement EMPTY_STATEMENT = new Statement(0, -1) {
        @Override
        public void execute() {
            // do nothing
        }

        @Override
        public boolean contentEquals(Statement statement) {
            return false;
        }

        @Override
        public List<? extends Statement> getChildren() {
            return Collections.emptyList();
        }
    };

    private final int startInd;
    private final int endInd;

    public Statement(int startInd, int endInd) {
        this.startInd = startInd;
        this.endInd = endInd;
    }

    public abstract void execute();

    public abstract List<? extends Statement> getChildren();

    /** Checks that statements are equal exclude their children
     * @param statement another Statement
     * @return true if statement classes are equal and they have equal length and position (and content in concrete statements)
     */
    public boolean contentEquals(Statement statement) { // this inspection does not fit the current context.
        if (this == statement) return true;
        return statement != null
                && getClass() == statement.getClass()
                && getStartInd() == statement.getStartInd()
                && getEndInd() == statement.getEndInd();
    }

    public int getStartInd() {
        return startInd;
    }

    public int getEndInd() {
        return endInd;
    }
}
