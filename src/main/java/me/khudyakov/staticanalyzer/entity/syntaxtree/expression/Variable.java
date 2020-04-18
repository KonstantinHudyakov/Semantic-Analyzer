package me.khudyakov.staticanalyzer.entity.syntaxtree.expression;

import me.khudyakov.staticanalyzer.util.ExpressionExecutionException;

import java.util.Objects;

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
    public int execute() throws ExpressionExecutionException {
        if(value == null) {
            throw new ExpressionExecutionException(String.format("Variable %s is not defined", name));
        }
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Variable variable = (Variable) o;
        return name.equals(variable.name) &&
                Objects.equals(value, variable.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
