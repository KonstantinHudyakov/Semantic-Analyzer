package me.khudyakov.staticanalyzer.entity.syntaxtree.expression;

public class Constant extends Expression {

    private final int value;

    public Constant(int value) {
        this.value = value;
    }

    @Override
    public int execute() {
        return 0;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
