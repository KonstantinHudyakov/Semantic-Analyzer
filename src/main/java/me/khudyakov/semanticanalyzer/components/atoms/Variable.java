package me.khudyakov.semanticanalyzer.components.atoms;

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
}
