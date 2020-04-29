package me.khudyakov.staticanalyzer.entity.syntaxtree.statement;

import java.util.Collections;
import java.util.List;

public abstract class Statement {

    public static final Statement EMPTY_STATEMENT = new Statement() {

        @Override
        public boolean contentEquals(Statement statement) {
            return false;
        }

        @Override
        public List<? extends Statement> getChildren() {
            return Collections.emptyList();
        }
    };

    /**
     * Next node in SyntaxTree in DFS order
     */
    private Statement next;

    public abstract List<? extends Statement> getChildren();

    /** Checks that statement's contents are equal exclude their children
     * @param statement another Statement
     * @return true if statement classes are equal and they have equal content
     */
    public abstract boolean contentEquals(Statement statement);

    public Statement next() {
        return next;
    }

    public void setNext(Statement next) {
        this.next = next;
    }
}
