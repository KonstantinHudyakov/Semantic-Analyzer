package me.khudyakov.staticanalyzer.entity.syntaxtree.expression;

import java.util.Objects;

public class Constant extends Expression {

    private final int value;

    public Constant(int value) {
        this.value = value;
    }

    @Override
    public int execute() {
        return value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Constant constant = (Constant) o;
        return value == constant.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
