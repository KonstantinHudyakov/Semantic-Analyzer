package me.khudyakov.staticanalyzer.components.atoms;

import me.khudyakov.staticanalyzer.components.Token;

public abstract class Atom extends Token {
    protected Integer value;

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
