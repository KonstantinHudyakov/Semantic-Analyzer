package me.khudyakov.staticanalyzer.entity;

public enum TokenType {
    // keywords
    IF,
    ASSIGN_IDENTIFIER, // @
    ASSIGN, // =

    // delimiters
    OPEN_PARENTHESIS,
    CLOSE_PARENTHESIS,
    OPEN_BRACE,
    CLOSE_BRACE,
    SEMICOLON,

    // expression operations
    LESS,
    GREATER,
    ADDITION,
    SUBTRACTION,
    MULTIPLICATION,
    DIVISION,

    // atoms
    INTEGER,
    IDENTIFIER
}
