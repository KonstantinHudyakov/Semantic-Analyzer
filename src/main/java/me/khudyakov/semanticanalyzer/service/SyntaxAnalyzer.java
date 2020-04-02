package me.khudyakov.semanticanalyzer.service;

import me.khudyakov.semanticanalyzer.program.ProgramCode;
import me.khudyakov.semanticanalyzer.program.SyntaxTree;
import me.khudyakov.semanticanalyzer.util.ExpressionConverterException;
import me.khudyakov.semanticanalyzer.util.ExpressionExecutionException;
import me.khudyakov.semanticanalyzer.util.SyntaxAnalyzerException;

public interface SyntaxAnalyzer {

    SyntaxTree analyze(ProgramCode programCode) throws SyntaxAnalyzerException, ExpressionConverterException, ExpressionExecutionException;
}
