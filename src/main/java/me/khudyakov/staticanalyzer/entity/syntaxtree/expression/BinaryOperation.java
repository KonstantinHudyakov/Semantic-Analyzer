package me.khudyakov.staticanalyzer.entity.syntaxtree.expression;

import me.khudyakov.staticanalyzer.entity.syntaxtree.expression.operator.BinaryOperator;

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
    public int execute() {
        return operator.applyAsInt(leftExpr.execute(), rightExpr.execute());
    }
}
