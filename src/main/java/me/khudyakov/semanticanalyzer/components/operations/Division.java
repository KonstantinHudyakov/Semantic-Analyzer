package me.khudyakov.semanticanalyzer.components.operations;

public class Division extends BinaryOperation {

    @Override
    public String toString() {
        return "/";
    }

    @Override
    public int applyAsInt(Integer a, Integer b) {
        return a / b;
    }
}
