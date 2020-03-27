package me.khudyakov.semanticanalyzer.components.semantictree;

import me.khudyakov.semanticanalyzer.program.Expression;
import me.khudyakov.semanticanalyzer.components.atoms.Constant;

public class ExpressionNode extends StatementNode {

    public ExpressionNode(Expression expression) {
        super(expression);
    }

    public ExpressionNode(Expression expression, int startInd, int endInd) {
        super(expression, startInd, endInd);
    }

    @Override
    public void execute() {
        Constant res = expression.execute();
        System.out.println(res.getValue());
    }
}
