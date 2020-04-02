package me.khudyakov.semanticanalyzer.components.syntaxtree;

import me.khudyakov.semanticanalyzer.components.atoms.Constant;
import me.khudyakov.semanticanalyzer.editor.OutputAreaWriter;
import me.khudyakov.semanticanalyzer.program.Expression;
import me.khudyakov.semanticanalyzer.util.ExpressionExecutionException;

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
