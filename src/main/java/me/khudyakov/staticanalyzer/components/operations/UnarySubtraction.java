package me.khudyakov.staticanalyzer.components.operations;

public class UnarySubtraction extends UnaryOperation {

    @Override
    public String toString() {
        return "-";
    }

    @Override
    public Integer apply(int a) {
        return -a;
    }
}