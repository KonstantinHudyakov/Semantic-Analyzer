package me.khudyakov.staticanalyzer.components.operations;

public class Multiplication extends BinaryOperation {

    @Override
    public String toString() {
        return "*";
    }

    @Override
    public int applyAsInt(Integer a, Integer b) {
        return a * b;
    }
}
