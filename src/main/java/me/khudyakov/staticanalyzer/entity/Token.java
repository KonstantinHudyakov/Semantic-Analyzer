package me.khudyakov.staticanalyzer.entity;

public class Token {

    private final String value;
    private final TokenType type;

    public Token(String value, TokenType type) {
        this.value = value;
        this.type = type;
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

    public boolean isTypeOf(TokenType type) {
        return this.type == type;
    }
}
