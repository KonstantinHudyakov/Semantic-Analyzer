package me.khudyakov.semanticanalyzer.components.atoms;

import me.khudyakov.semanticanalyzer.components.Lexeme;

public abstract class Atom extends Lexeme {
    protected Integer value;

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
