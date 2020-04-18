package me.khudyakov.staticanalyzer.entity.syntaxtree.expression;

import me.khudyakov.staticanalyzer.entity.syntaxtree.expression.operator.BinaryOperator;
import me.khudyakov.staticanalyzer.util.ExpressionExecutionException;

import java.util.Objects;

public class BinaryOperation extends Expression {

    private final BinaryOperator operator;
    private final Expression leftExpr;
    private final Expression rightExpr;

    public BinaryOperation(BinaryOperator operator, Expression leftExpr, Expression rightExpr) {
        this.operator = operator;
        this.leftExpr = leftExpr;
        this.rightExpr = rightExpr;
    }

    @Override
    public int execute() throws ExpressionExecutionException {
        try {
            return operator.applyAsInt(leftExpr.execute(), rightExpr.execute());
        } catch (ArithmeticException ex) {
            throw new ExpressionExecutionException("Division by zero in operation: " + toString());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BinaryOperation operation = (BinaryOperation) o;
        return operator.getClass() == operation.operator.getClass() &&
                leftExpr.equals(operation.leftExpr) &&
                rightExpr.equals(operation.rightExpr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(operator, leftExpr, rightExpr);
    }

    @Override
    public String toString() {
        return String.format("%s %s %s", leftExpr.toString(), operator.toString(), rightExpr.toString());
    }
}
