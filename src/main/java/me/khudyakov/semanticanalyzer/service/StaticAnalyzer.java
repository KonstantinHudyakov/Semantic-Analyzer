package me.khudyakov.semanticanalyzer.service;

import me.khudyakov.semanticanalyzer.program.ProgramCode;
import me.khudyakov.semanticanalyzer.program.SemanticTree;
import me.khudyakov.semanticanalyzer.util.ExpressionConverterException;
import me.khudyakov.semanticanalyzer.util.ExpressionExecutionException;
import me.khudyakov.semanticanalyzer.util.StaticAnalyzerException;

public interface StaticAnalyzer {

    SemanticTree analyze(ProgramCode programCode) throws StaticAnalyzerException, ExpressionConverterException, ExpressionExecutionException;
}
