package me.khudyakov.semanticanalyzer.program;

import me.khudyakov.semanticanalyzer.components.Lexeme;
import me.khudyakov.semanticanalyzer.components.atoms.Atom;
import me.khudyakov.semanticanalyzer.components.atoms.Constant;
import me.khudyakov.semanticanalyzer.components.operations.*;
import me.khudyakov.semanticanalyzer.util.ExpressionExecutionException;

import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

public class Expression {

    private List<Lexeme> expr;

    public Constant execute() throws ExpressionExecutionException {
        try {
            Stack<Integer> st = new Stack<>();
            for (int i = 0; i < expr.size(); i++) {
                Lexeme cur = expr.get(i);
                if (cur instanceof Atom) {
                    st.push(((Atom) cur).getValue());
                } else if (cur instanceof UnaryOperation) {
                    int a = st.pop();
                    st.push(((UnaryOperation) cur).apply(a));
                } else if (cur instanceof BinaryOperation) {
                    int a = st.pop();
                    int b = st.pop();
                    st.push(((BinaryOperation) cur).applyAsInt(b, a));
                }
            }
            return new Constant(st.peek());
        } catch (Exception ex) {
            throw new ExpressionExecutionException(ex);
        }
    }

    public Expression(List<Lexeme> expr) {
        this.expr = expr;
    }

    public List<Lexeme> getExpr() {
        return expr;
    }

    public void setExpr(List<Lexeme> expr) {
        this.expr = expr;
    }

    public int size() {
        return expr.size();
    }

    public boolean isEmpty() {
        return expr.isEmpty();
    }

    public boolean contains(Object o) {
        return expr.contains(o);
    }

    public Iterator<Lexeme> iterator() {
        return expr.iterator();
    }

    public Lexeme get(int index) {
        return expr.get(index);
    }

    public int indexOf(Object o) {
        return expr.indexOf(o);
    }

    public int lastIndexOf(Object o) {
        return expr.lastIndexOf(o);
    }

    public List<Lexeme> subList(int fromIndex, int toIndex) {
        return expr.subList(fromIndex, toIndex);
    }

    public String toString() {
        return expr.stream()
                   .map(Lexeme::toString)
                   .collect(Collectors.joining());
    }
}
