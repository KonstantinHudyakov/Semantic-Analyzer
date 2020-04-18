package me.khudyakov.staticanalyzer.util;

import me.khudyakov.staticanalyzer.entity.Token;
import me.khudyakov.staticanalyzer.entity.TokenType;

import static me.khudyakov.staticanalyzer.entity.TokenType.*;
import static me.khudyakov.staticanalyzer.entity.TokenType.ASSIGN_IDENTIFIER;

public class TokenUtils {

    public static boolean isExprStart(Token token) {
        TokenType type = token.getType();
        return type == OPEN_PARENTHESIS
                || type == IDENTIFIER
                || type == INTEGER;
    }

    public static boolean isStatementStart(Token token) {
        TokenType type = token.getType();
        return type == OPEN_BRACE
                || type == IF
                || type == ASSIGN_IDENTIFIER
                || isExprStart(token);
    }

    public static boolean isMultiplyDivision(Token token) {
        TokenType type = token.getType();
        return type == MULTIPLICATION
                || type == DIVISION;
    }

    public static boolean isPlusMinus(Token token) {
        TokenType type = token.getType();
        return type == ADDITION
                || type == SUBTRACTION;
    }

    public static boolean isCompareOperation(Token token) {
        TokenType type = token.getType();
        return type == GREATER
                || type == LESS;
    }


    public static boolean isTokenOfType(Token token, TokenType type) {
        return token.getType() == type;
    }
}
