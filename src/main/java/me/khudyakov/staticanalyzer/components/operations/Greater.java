package me.khudyakov.staticanalyzer.components.operations;

public class Greater extends BinaryOperation {

    @Override
    public String toString() {
        return ">";
    }

    @Override
    public int applyAsInt(Integer a, Integer b) {
        return a > b ? 1 : 0;
    }
}
