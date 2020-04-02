package me.khudyakov.staticanalyzer.components.atoms;

public class Constant extends Atom {

    public Constant(Integer value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
