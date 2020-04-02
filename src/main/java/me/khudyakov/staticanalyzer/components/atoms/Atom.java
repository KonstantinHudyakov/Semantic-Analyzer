package me.khudyakov.staticanalyzer.components.atoms;

import me.khudyakov.staticanalyzer.components.Lexeme;

public abstract class Atom extends Lexeme {
    protected Integer value;

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
