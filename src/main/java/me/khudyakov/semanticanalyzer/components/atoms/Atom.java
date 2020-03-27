package me.khudyakov.semanticanalyzer.components.atoms;

import me.khudyakov.semanticanalyzer.components.Lexeme;

public abstract class Atom extends Lexeme {
    protected int value;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
