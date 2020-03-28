package me.khudyakov.semanticanalyzer.service;

import me.khudyakov.semanticanalyzer.components.Lexeme;
import me.khudyakov.semanticanalyzer.components.atoms.Atom;
import me.khudyakov.semanticanalyzer.components.brackets.CloseParenthesis;
import me.khudyakov.semanticanalyzer.components.brackets.OpenParenthesis;
import me.khudyakov.semanticanalyzer.components.operations.*;
import me.khudyakov.semanticanalyzer.program.Expression;
import me.khudyakov.semanticanalyzer.util.ExpressionConverterException;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ExpressionConverterImpl implements ExpressionConverter {

    private final static String ERROR_MESSAGE_FORM = "Ошибка преобразования в постфиксную форму: ind = %d, expr = %s";

    public void convertToPostfix(Expression expr) throws ExpressionConverterException {
        Stack<Lexeme> st = new Stack<>();
        List<Lexeme> postfix = new ArrayList<>();
        int openParenthesisNum = 0;
        for (int i = 0; i < expr.size(); i++) {
            Lexeme cur = expr.get(i);
            if (cur instanceof Atom) {
                postfix.add(cur);
            } else if (cur instanceof UnarySubtraction) {
                st.push(cur);
            } else if (cur instanceof OpenParenthesis) {
                openParenthesisNum++;
                if (i - 1 >= 0 && (expr.get(i - 1) instanceof Atom || expr.get(i - 1) instanceof CloseParenthesis))
                    throw new ExpressionConverterException(String.format(ERROR_MESSAGE_FORM, i, expr.toString()));
                st.push(cur);
            } else if (cur instanceof CloseParenthesis) {
                if (openParenthesisNum < 1 || expr.get(i - 1) instanceof OpenParenthesis || expr.get(i - 1) instanceof Operation)
                    throw new ExpressionConverterException(String.format(ERROR_MESSAGE_FORM, i, expr.toString()));
                while (!st.empty() && !(st.peek() instanceof OpenParenthesis)) {
                    postfix.add(st.peek());
                    st.pop();
                }
                if (st.empty())
                    throw new ExpressionConverterException(String.format(ERROR_MESSAGE_FORM, i, expr.toString()));
                st.pop();
            } else if (cur instanceof Operation) {
                while (!st.empty()) {
                    if (!(st.peek() instanceof Operation)) {
                        break;
                    }
                    Operation top = (Operation) st.peek();
                    if (priority(top) > priority((Operation) cur)) {
                        postfix.add(top);
                        st.pop();
                    } else {
                        break;
                    }
                }
                st.push(cur);
            } else {
                throw new ExpressionConverterException(String.format(ERROR_MESSAGE_FORM, i, expr.toString()));
            }
        }

        while (!st.empty()) {
            if (!(st.peek() instanceof Operation) && !(st.peek() instanceof Atom)) {
                throw new ExpressionConverterException(String.format(ERROR_MESSAGE_FORM, expr.size(), expr.toString()));
            }
            postfix.add(st.peek());
            st.pop();
        }

        if(!checkExecution(postfix)) {
            throw new ExpressionConverterException("Ошибка при вычислении выражения: " + expr);
        }
        expr.setExpr(postfix);
    }

    private boolean checkExecution(List<Lexeme> expr) {
        int stackSize = 0;
        for (Lexeme cur : expr) {
            if (cur instanceof Atom) {
                stackSize++;
            } else if (cur instanceof UnaryOperation) {
                if (stackSize < 1) {
                    return false;
                }
            } else if (cur instanceof BinaryOperation) {
                if (stackSize < 2) {
                    return false;
                }
                stackSize--;
            }
        }
        return stackSize == 1;
    }

    private int priority(Operation operation) {
        int priority = -1;
        Class<?> operationClass = operation.getClass();
        if (operationClass.equals(UnarySubtraction.class)) {
            priority = 6;
        } else if (operationClass.equals(Division.class)) {
            priority = 5;
        } else if (operationClass.equals(Multiplication.class)) {
            priority = 4;
        } else if (operationClass.equals(Subtraction.class)) {
            priority = 3;
        } else if (operationClass.equals(Addition.class)) {
            priority = 2;
        } else if (operationClass.equals(Greater.class) || operationClass.equals(Less.class)) {
            priority = 1;
        }
        return priority;
    }
}
