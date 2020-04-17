package me.khudyakov.staticanalyzer.entity;

public enum TokenType {
    // keywords
    IF("IF"),
    ASSIGN_IDENTIFIER("ASSIGN_IDENTIFIER"), // @
    ASSIGN("ASSIGN"), // =

    // delimiters
    OPEN_PARENTHESIS("OPEN_PARENTHESIS"),
    CLOSE_PARENTHESIS("CLOSE_PARENTHESIS"),
    OPEN_BRACE("OPEN_BRACE"),
    CLOSE_BRACE("CLOSE_BRACE"),
    SEMICOLON("SEMICOLON"),

    // expression operations
    LESS("LESS"),
    GREATER("GREATER"),
    ADDITION("ADDITION"),
    SUBTRACTION("SUBTRACTION"),
    MULTIPLICATION("MULTIPLICATION"),
    DIVISION("DIVISION"),

    // atoms
    INTEGER("INTEGER"),
    IDENTIFIER("IDENTIFIER");


    private final String value;

    TokenType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
