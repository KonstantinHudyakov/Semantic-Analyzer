package me.khudyakov.staticanalyzer.components.atoms;

import java.util.Objects;

public class Variable extends Atom {
    private String name;

    public Variable(String name) {
        this.name = name;
    }

    public Variable(String name, Integer value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        Variable variable = (Variable) o;
        return Objects.equals(value, variable.getValue())
                && Objects.equals(name, variable.getName());
    }
}
