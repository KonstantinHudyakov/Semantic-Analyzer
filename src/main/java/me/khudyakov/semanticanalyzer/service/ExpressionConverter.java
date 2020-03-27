package me.khudyakov.semanticanalyzer.service;

import me.khudyakov.semanticanalyzer.program.Expression;
import me.khudyakov.semanticanalyzer.util.ExpressionConverterException;

public interface ExpressionConverter {

    void convertToPostfix(Expression expr) throws ExpressionConverterException;
}
