package me.khudyakov.staticanalyzer.program;

import me.khudyakov.staticanalyzer.components.Token;

import java.util.Iterator;
import java.util.List;

public class ProgramCode {

    private List<Token> program;

    public ProgramCode(List<Token> program) {
        this.program = program;
    }

    public void setProgram(List<Token> program) {
        this.program = program;
    }

    public int size() {
        return program.size();
    }

    public boolean isEmpty() {
        return program.isEmpty();
    }

    public boolean contains(Object o) {
        return program.contains(o);
    }

    public Iterator<Token> iterator() {
        return program.iterator();
    }

    public Token get(int index) {
        return program.get(index);
    }

    public int indexOf(Object o) {
        return program.indexOf(o);
    }

    public int lastIndexOf(Object o) {
        return program.lastIndexOf(o);
    }

    public List<Token> subList(int fromIndex, int toIndex) {
        return program.subList(fromIndex, toIndex);
    }

    public String toString() {
        return program.toString();
    }
}
