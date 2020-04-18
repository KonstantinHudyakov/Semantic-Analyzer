package me.khudyakov.staticanalyzer.entity.syntaxtree.expression.operator;

public class GreaterOperator implements BinaryOperator {

    @Override
    public int applyAsInt(Integer integer, Integer integer2) {
        return integer > integer2 ? 1 : 0;
    }

    @Override
    public String toString() {
        return ">";
    }
}
