package me.khudyakov.staticanalyzer.components.operations;

public class Addition extends BinaryOperation {

    @Override
    public String toString() {
        return "+";
    }

    @Override
    public int applyAsInt(Integer a, Integer b) {
        return a + b;
    }
}
