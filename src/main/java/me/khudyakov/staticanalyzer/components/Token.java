package me.khudyakov.staticanalyzer.components;

public abstract class Token {

    public abstract String toString();

    @Override
    public boolean equals(Object o) {
        return this == o || o != null && getClass() == o.getClass();
    }
}
