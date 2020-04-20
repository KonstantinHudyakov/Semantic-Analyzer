package me.khudyakov.staticanalyzer.service;

import me.khudyakov.staticanalyzer.entity.Token;
import me.khudyakov.staticanalyzer.entity.ProgramCode;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static me.khudyakov.staticanalyzer.entity.TokenType.*;

public class CodeParserImpl implements CodeParser {

    public ProgramCode parse(String text) throws ParseException {
        List<Token> code = new ArrayList<>();
        char[] arr = text.toCharArray();
        Token token;
        int curInd = skipDelimiters(text, 0);
        while (curInd < text.length()) {
            switch (arr[curInd]) {
                case '@': {
                    token = new Token("@", ASSIGN_IDENTIFIER, curInd);
                    break;
                }
                case '=': {
                    token = new Token("=", ASSIGN, curInd);
                    break;
                }
                case ';': {
                    token = new Token(";", SEMICOLON, curInd);
                    break;
                }
                case '(': {
                    token = new Token("(", OPEN_PARENTHESIS, curInd);
                    break;
                }
                case ')': {
                    token = new Token(")", CLOSE_PARENTHESIS, curInd);
                    break;
                }
                case '{': {
                    token = new Token("{", OPEN_BRACE, curInd);
                    break;
                }
                case '}': {
                    token = new Token("}", CLOSE_BRACE, curInd);
                    break;
                }
                case '+': {
                    if(curInd + 1 < text.length() && Character.isDigit(arr[curInd + 1])) {
                        // it is integer with sign +
                        token = readInteger(text, curInd);
                        curInd += token.getValue().length() - 1;
                    } else {
                        token = new Token("+", ADDITION, curInd);
                    }
                    break;
                }
                case '-': {
                    if(curInd + 1 < text.length() && Character.isDigit(arr[curInd + 1])) {
                        // it is integer with sign -
                        token = readInteger(text, curInd);
                        curInd += token.getValue().length() - 1;
                    } else {
                        token = new Token("-", SUBTRACTION, curInd);
                    }
                    break;
                }
                case '*': {
                    token = new Token("*", MULTIPLICATION, curInd);
                    break;
                }
                case '/': {
                    if(curInd + 1 < text.length() && arr[curInd + 1] == '/') {
                        // it is a comment
                        token = readComment(text, curInd);
                        curInd += token.getValue().length() - 1;
                    } else {
                        token = new Token("/", DIVISION, curInd);
                    }
                    break;
                }
                case '>': {
                    token = new Token(">", GREATER, curInd);
                    break;
                }
                case '<': {
                    token = new Token("<", LESS, curInd);
                    break;
                }
                default: {
                    if (Character.isDigit(arr[curInd])) {
                        token = readInteger(text, curInd);
                    } else if (Character.isLetter(arr[curInd])) {
                        token = readIdentifier(text, curInd);
                        if("if".equals(token.getValue())) {
                            token = new Token("if", IF, curInd);
                        }
                    } else {
                        throw new ParseException(String.format("Некорректный символ '%s' в выражении \"%s\"", arr[curInd], text), curInd);
                    }
                    curInd += token.getValue().length() - 1;
                }
            }
            code.add(token);
            curInd = skipDelimiters(text, curInd + 1);
        }

        return new ProgramCode(code);
    }

    private Token readInteger(String text, int fromInd) throws ParseException {
        StringBuilder intBuilder = new StringBuilder();
        int curInd = fromInd;
        if(text.charAt(curInd) == '-') {
            intBuilder.append('-');
            curInd++;
        } else if(text.charAt(curInd) == '+') {
            curInd++;
        }
        while (curInd < text.length() && Character.isDigit(text.charAt(curInd))) {
            intBuilder.append(text.charAt(curInd));
            curInd++;
        }
        try {
            // check that value belongs to the Integer interval
            int constant = Integer.parseInt(intBuilder.toString());
            return new Token(String.valueOf(constant), INTEGER, fromInd);
        } catch (NumberFormatException ex) {
            throw new ParseException("Numerical value is out of range for type Integer, value = " + intBuilder.toString(), fromInd);
        }
    }

    private Token readIdentifier(String text, int fromInd) {
        StringBuilder idBuilder = new StringBuilder();
        int curInd = fromInd;
        while (curInd < text.length() && Character.isLetter(text.charAt(curInd))) {
            idBuilder.append(text.charAt(curInd));
            curInd++;
        }
        return new Token(idBuilder.toString(), IDENTIFIER, fromInd);
    }

    private Token readComment(String text, int fromInd) {
        StringBuilder commentBuilder = new StringBuilder();
        int curInd = fromInd;
        while(curInd < text.length() && text.charAt(curInd) != '\n') {
            commentBuilder.append(text.charAt(curInd));
            curInd++;
        }
        return new Token(commentBuilder.toString(), COMMENT, fromInd);
    }

    private int skipDelimiters(String text, int fromInd) {
        while(fromInd < text.length() && isDelimiter(text.charAt(fromInd))) {
            fromInd++;
        }
        return fromInd;
    }

    private boolean isDelimiter(char ch) {
        return ch == ' ' || ch == '\n' || ch == '\r' || ch == '\t';
    }
}
