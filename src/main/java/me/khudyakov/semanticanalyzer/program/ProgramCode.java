package me.khudyakov.semanticanalyzer.program;

import me.khudyakov.semanticanalyzer.components.Lexeme;

import java.util.Iterator;
import java.util.List;

public class ProgramCode {

    private List<Lexeme> program;

    public ProgramCode(List<Lexeme> program) {
        this.program = program;
    }

    public void setProgram(List<Lexeme> program) {
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

    public Iterator<Lexeme> iterator() {
        return program.iterator();
    }

    public Lexeme get(int index) {
        return program.get(index);
    }

    public int indexOf(Object o) {
        return program.indexOf(o);
    }

    public int lastIndexOf(Object o) {
        return program.lastIndexOf(o);
    }

    public List<Lexeme> subList(int fromIndex, int toIndex) {
        return program.subList(fromIndex, toIndex);
    }

    public String toString() {
        return program.toString();
    }
}
