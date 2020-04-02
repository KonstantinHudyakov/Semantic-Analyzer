package me.khudyakov.staticanalyzer.components.syntaxtree;

import me.khudyakov.staticanalyzer.components.atoms.Constant;
import me.khudyakov.staticanalyzer.editor.OutputAreaWriter;
import me.khudyakov.staticanalyzer.program.Expression;
import me.khudyakov.staticanalyzer.util.ExpressionExecutionException;

public class ExpressionNode extends StatementNode {

    public ExpressionNode(Expression expression) {
        super(expression);
    }

    public ExpressionNode(Expression expression, int startInd, int endInd) {
        super(expression, startInd, endInd);
    }

    @Override
    public void executeSubtree() throws ExpressionExecutionException {
        Constant res = expression.execute();
        OutputAreaWriter.println(res.toString());
    }
}
