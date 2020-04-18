package me.khudyakov.staticanalyzer.entity.syntaxtree.expression;

import me.khudyakov.staticanalyzer.util.ExpressionExecutionException;

public abstract class Expression {

    public static final Expression EMPTY_EXPRESSION = new Expression() {
        @Override
        public int execute() throws ExpressionExecutionException {
            throw new ExpressionExecutionException("Empty expression");
        }
    };

    public abstract int execute() throws ExpressionExecutionException;
}
