package me.khudyakov.staticanalyzer.service;

import me.khudyakov.staticanalyzer.editor.OutputAreaWriter;
import me.khudyakov.staticanalyzer.entity.syntaxtree.expression.Expression;
import me.khudyakov.staticanalyzer.entity.syntaxtree.expression.Variable;
import me.khudyakov.staticanalyzer.entity.syntaxtree.statement.*;

public class ProgramExecutionVisitor implements SyntaxTreeVisitor {

    @Override
    public void visit(AssignStatement assignStatement) {
        Expression expr = assignStatement.getExpr();
        Variable variable = assignStatement.getVariable();
        int value = expr.execute();
        variable.setValue(value);
    }

    @Override
    public void visit(ExpressionStatement expressionStatement) {
        int value = expressionStatement.getExpression().execute();
        OutputAreaWriter.println(String.valueOf(value));
    }

    @Override
    public void visit(IfStatement ifStatement) {
        Expression condition = ifStatement.getCondition();
        if(condition.execute() != 0) {
            visit(ifStatement.getBody());
        }
    }

    @Override
    public void visit(BlockStatement blockStatement) {
        blockStatement.stream().forEach(this::visit);
    }
}
