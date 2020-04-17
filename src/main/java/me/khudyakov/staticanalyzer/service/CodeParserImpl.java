package me.khudyakov.staticanalyzer.service;

import me.khudyakov.staticanalyzer.entity.Token;
import me.khudyakov.staticanalyzer.program.ProgramCode;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static me.khudyakov.staticanalyzer.entity.TokenType.*;

public class CodeParserImpl implements CodeParser {

    public ProgramCode parse(String text) throws ParseException {
        List<Token> code = new ArrayList<>();
        text = text.trim().replaceAll("[\r\n\t]+", " ");
        String[] words = text.split(" +");
        for (String word : words) {
            code.addAll(parseTokens(word));
        }
        return new ProgramCode(code);
    }

    private List<Token> parseTokens(String word) throws ParseException {
        List<Token> code = new ArrayList<>();
        char[] arr = word.toCharArray();
        int n = word.length();
        Token token = null;
        for (int i = 0; i < n; i++) {
            switch (arr[i]) {
                case '@': {
                    token = new Token("@", ASSIGN_IDENTIFIER);
                    break;
                }
                case '=': {
                    token = new Token("=", ASSIGN);
                    break;
                }
                case ';': {
                    token = new Token(";", SEMICOLON);
                    break;
                }
                case '(': {
                    token = new Token("(", OPEN_PARENTHESIS);
                    break;
                }
                case ')': {
                    token = new Token(")", CLOSE_PARENTHESIS);
                    break;
                }
                case '{': {
                    token = new Token("{", OPEN_BRACE);
                    break;
                }
                case '}': {
                    token = new Token("}", CLOSE_BRACE);
                    break;
                }
                case '+': {
                    if(i + 1 < n && Character.isDigit(arr[i + 1])) {
                        // it is integer with sign +
                        token = readInteger(word, i);
                        i += token.getValue().length() - 1;
                    } else {
                        token = new Token("+", ADDITION);
                    }
                    break;
                }
                case '-': {
                    if(i + 1 < n && Character.isDigit(arr[i + 1])) {
                        // it is integer with sign -
                        token = readInteger(word, i);
                        i += token.getValue().length() - 1;
                    } else {
                        token = new Token("-", SUBTRACTION);
                    }
                    break;
                }
                case '*': {
                    token = new Token("*", MULTIPLICATION);
                    break;
                }
                case '/': {
                    token = new Token("/", DIVISION);
                    break;
                }
                case '>': {
                    token = new Token(">", GREATER);
                    break;
                }
                case '<': {
                    token = new Token("<", LESS);
                    break;
                }
                default: {
                    if (Character.isDigit(arr[i])) {
                        token = readInteger(word, i);
                    } else if (Character.isLetter(arr[i])) {
                        token = readIdentifier(word, i);
                    } else {
                        throw new ParseException(String.format("Некорректный символ '%s' в выражении \"%s\"", arr[i], word), i);
                    }
                    i += token.getValue().length() - 1;
                }
            }
            code.add(token);
        }
        return code;
    }

    private Token readInteger(String word, int fromInd) throws ParseException {
        StringBuilder intBuilder = new StringBuilder();
        if(word.charAt(fromInd) == '-') {
            intBuilder.append('-');
            fromInd++;
        } else if(word.charAt(fromInd) == '+') {
            fromInd++;
        }
        for(int i = fromInd; i < word.length() && Character.isDigit(word.charAt(i)); i++) {
            intBuilder.append(word.charAt(i));
        }
        try {
            // check that value belongs to the Integer interval
            int constant = Integer.parseInt(intBuilder.toString());
            return new Token(String.valueOf(constant), INTEGER);
        } catch (NumberFormatException ex) {
            throw new ParseException("Numerical value is out of range for type Integer, value = " + intBuilder.toString(), fromInd);
        }
    }

    private Token readIdentifier(String word, int fromInd) {
        StringBuilder idBuilder = new StringBuilder();
        for(int i = fromInd; i < word.length() && Character.isLetter(word.charAt(i)); i++) {
            idBuilder.append(word.charAt(i));
        }
        return new Token(idBuilder.toString(), IDENTIFIER);
    }
}
