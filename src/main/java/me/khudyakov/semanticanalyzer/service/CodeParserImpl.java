package me.khudyakov.semanticanalyzer.service;

import me.khudyakov.semanticanalyzer.components.*;
import me.khudyakov.semanticanalyzer.components.atoms.Atom;
import me.khudyakov.semanticanalyzer.components.atoms.Constant;
import me.khudyakov.semanticanalyzer.components.atoms.Variable;
import me.khudyakov.semanticanalyzer.components.brackets.CloseBrace;
import me.khudyakov.semanticanalyzer.components.brackets.CloseParenthesis;
import me.khudyakov.semanticanalyzer.components.brackets.OpenBrace;
import me.khudyakov.semanticanalyzer.components.brackets.OpenParenthesis;
import me.khudyakov.semanticanalyzer.components.operations.*;
import me.khudyakov.semanticanalyzer.program.ProgramCode;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CodeParserImpl implements CodeParser {

    public ProgramCode parse(String text) throws ParseException {
        List<Lexeme> code = new ArrayList<>();
        Map<String, Variable> variables = new HashMap<>();
        text = text.trim().replaceAll("\n", " ");
        text = text.replaceAll("\t", " ");
        String[] tokens = text.split(" +");
        for (String token : tokens) {
            parseToken(token, code, variables);
        }
        return new ProgramCode(code);
    }

    private void parseToken(String token, List<Lexeme> lexemes, Map<String, Variable> variables) throws ParseException {
        char[] arr = token.toCharArray();
        int n = token.length();
        for (int i = 0; i < n; i++) {
            switch (arr[i]) {
                case '@': {
                    lexemes.add(new AssignIdentifier());
                    break;
                }
                case '=': {
                    lexemes.add(new Assign());
                    break;
                }
                case ';': {
                    lexemes.add(new Semicolon());
                    break;
                }
                case '(': {
                    lexemes.add(new OpenParenthesis());
                    break;
                }
                case ')': {
                    lexemes.add(new CloseParenthesis());
                    break;
                }
                case '{': {
                    lexemes.add(new OpenBrace());
                    break;
                }
                case '}': {
                    lexemes.add(new CloseBrace());
                    break;
                }
                case '+': {
                    lexemes.add(new Addition());
                    break;
                }
                case '*': {
                    lexemes.add(new Multiplication());
                    break;
                }
                case '/': {
                    lexemes.add(new Division());
                    break;
                }
                case '>': {
                    lexemes.add(new Greater());
                    break;
                }
                case '<': {
                    lexemes.add(new Less());
                    break;
                }
                case '-': {
                    if (!lexemes.isEmpty() && lexemes.get(lexemes.size() - 1) instanceof Atom) {
                        lexemes.add(new Subtraction());
                    } else {
                        lexemes.add(new UnarySubtraction());
                    }
                    break;
                }
                default: {
                    if (Character.isDigit(arr[i])) {
                        StringBuilder numBuilder = new StringBuilder();
                        numBuilder.append(arr[i]);
                        while (i + 1 < n && Character.isDigit(arr[i + 1])) {
                            numBuilder.append(arr[i + 1]);
                            i++;
                        }
                        int constant = Integer.parseInt(numBuilder.toString());
                        lexemes.add(new Constant(constant));
                    } else if (Character.isLetter(arr[i])) {
                        StringBuilder varBuilder = new StringBuilder();
                        varBuilder.append(arr[i]);
                        while (i + 1 < n && Character.isLetter(arr[i + 1])) {
                            varBuilder.append(arr[i + 1]);
                            i++;
                        }
                        String var = varBuilder.toString();
                        if("if".equals(var)) {
                            lexemes.add(new IfStatement());
                        } else {
                            // Если такая переменная уже объявлена, то добавляем ссылку на неё, а не создаём заново
                            Variable variable = null;
                            if(variables.containsKey(var)) {
                                variable = variables.get(var);
                            } else {
                                variable = new Variable(var);
                                variables.put(var, variable);
                            }
                            lexemes.add(variable);
                        }
                    } else {
                        throw new ParseException(String.format("Некорректный символ '%s' в выражении \"%s\"", arr[i], token), i);
                    }
                }
            }
        }
    }
}
