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

    /**
     * Next node in SyntaxTree in DFS order
     */
    private Statement next;

    public Statement(int startInd, int endInd) {
        this.startInd = startInd;
        this.endInd = endInd;
    }

    public abstract void execute();

    public abstract List<? extends Statement> getChildren();

    /** Checks that statement's contents are equal exclude their children
     * @param statement another Statement
     * @return true if statement classes are equal and they have equal content
     */
    public abstract boolean contentEquals(Statement statement);

    public boolean rangeEquals(Statement statement) {
        return startInd == statement.getStartInd()
                && endInd == statement.getEndInd();
    }

    public boolean equals(Object obj) {
        if (this == obj) return true;
        if(obj instanceof Statement) {
            Statement statement = (Statement) obj;
            return rangeEquals(statement) && contentEquals(statement);
        }
        return false;
    }

    public int getStartInd() {
        return startInd;
    }

    public int getEndInd() {
        return endInd;
    }

    public Statement next() {
        return next;
    }

    public void setNext(Statement next) {
        this.next = next;
    }
}
