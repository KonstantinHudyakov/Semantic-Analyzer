package me.khudyakov.staticanalyzer.service;

import me.khudyakov.staticanalyzer.program.ProgramCode;
import me.khudyakov.staticanalyzer.program.SyntaxTree;
import me.khudyakov.staticanalyzer.util.ExpressionConverterException;
import me.khudyakov.staticanalyzer.util.ExpressionExecutionException;
import me.khudyakov.staticanalyzer.util.SyntaxAnalyzerException;

public interface SyntaxAnalyzer {

    SyntaxTree analyze(ProgramCode programCode) throws SyntaxAnalyzerException, ExpressionConverterException, ExpressionExecutionException;
}
