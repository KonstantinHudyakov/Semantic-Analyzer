package me.khudyakov.staticanalyzer.components.atoms;

import java.util.Objects;

public class Constant extends Atom {

    public Constant(Integer value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        Constant constant = (Constant) o;
        return Objects.equals(value, constant.getValue());
    }
}
