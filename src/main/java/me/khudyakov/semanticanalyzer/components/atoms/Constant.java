package me.khudyakov.semanticanalyzer.components.atoms;

public class Constant extends Atom {

    public Constant(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
