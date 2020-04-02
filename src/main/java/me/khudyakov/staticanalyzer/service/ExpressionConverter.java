package me.khudyakov.staticanalyzer.service;

import me.khudyakov.staticanalyzer.program.Expression;
import me.khudyakov.staticanalyzer.util.ExpressionConverterException;

public interface ExpressionConverter {

    void convertToPostfix(Expression expr) throws ExpressionConverterException;
}
