package me.khudyakov.staticanalyzer.program;

import me.khudyakov.staticanalyzer.components.Token;
import me.khudyakov.staticanalyzer.components.atoms.Atom;
import me.khudyakov.staticanalyzer.components.atoms.Constant;
import me.khudyakov.staticanalyzer.components.operations.*;
import me.khudyakov.staticanalyzer.util.ExpressionExecutionException;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Stack;
import java.util.stream.Collectors;

public class Expression {

    private List<Token> expr;

    public Constant execute() throws ExpressionExecutionException {
        try {
            Stack<Integer> st = new Stack<>();
            for (int i = 0; i < expr.size(); i++) {
                Token cur = expr.get(i);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Expression that = (Expression) o;
        return Objects.equals(expr, that.expr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(expr);
    }

    public Expression(List<Token> expr) {
        this.expr = expr;
    }

    public List<Token> getExpr() {
        return expr;
    }

    public void setExpr(List<Token> expr) {
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

    public Iterator<Token> iterator() {
        return expr.iterator();
    }

    public Token get(int index) {
        return expr.get(index);
    }

    public int indexOf(Object o) {
        return expr.indexOf(o);
    }

    public int lastIndexOf(Object o) {
        return expr.lastIndexOf(o);
    }

    public List<Token> subList(int fromIndex, int toIndex) {
        return expr.subList(fromIndex, toIndex);
    }

    public String toString() {
        return expr.stream()
                   .map(Token::toString)
                   .collect(Collectors.joining());
    }
}
