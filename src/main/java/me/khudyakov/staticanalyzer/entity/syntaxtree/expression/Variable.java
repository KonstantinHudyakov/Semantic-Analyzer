package me.khudyakov.staticanalyzer.entity.syntaxtree.expression;

public class Variable extends Expression {

    public final String name;
    public Integer value;

    public Variable(String name) {
        this.name = name;
    }

    public Variable(String name, Integer value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public int execute() {
        return value;
    }

    public String getName() {
        return name;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
