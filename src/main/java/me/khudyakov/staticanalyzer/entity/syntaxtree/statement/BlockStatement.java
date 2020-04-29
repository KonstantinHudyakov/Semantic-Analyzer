package me.khudyakov.staticanalyzer.entity.syntaxtree.statement;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class BlockStatement extends Statement {

    private final List<? extends Statement> statements;

    public BlockStatement(List<? extends Statement> statements) {
        this.statements = statements;
    }

    @Override
    public List<? extends Statement> getChildren() {
        return Collections.unmodifiableList(statements);
    }

    @Override
    public boolean contentEquals(Statement statement) {
        return getClass() == statement.getClass();
    }

    public int size() {
        return statements.size();
    }

    public Statement get(int index) {
        return statements.get(index);
    }

    public Stream<? extends Statement> stream() {
        return statements.stream();
    }
}
