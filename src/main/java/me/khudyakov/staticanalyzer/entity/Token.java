package me.khudyakov.staticanalyzer.entity;

public class Token {

    private final String value;
    private final TokenType type;
    private final int startOffset;

    public Token(String value, TokenType type, int startOffset) {
        this.value = value;
        this.type = type;
        this.startOffset = startOffset;
    }

    @Override
    public String toString() {
        return value;
    }

    public String getValue() {
        return value;
    }

    public TokenType getType() {
        return type;
    }

    public int getStartOffset() {
        return startOffset;
    }
}
