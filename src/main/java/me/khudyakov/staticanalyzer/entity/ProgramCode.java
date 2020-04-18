package me.khudyakov.staticanalyzer.entity;

import java.util.List;

public class ProgramCode {

    private final List<Token> program;

    public ProgramCode(List<Token> program) {
        this.program = program;
    }

    public int size() {
        return program.size();
    }

    public Token get(int index) {
        return program.get(index);
    }

    public List<Token> subProgram(int fromIndex, int toIndex) {
        return program.subList(fromIndex, toIndex);
    }

    public String toString() {
        return program.toString();
    }
}
