package me.khudyakov.staticanalyzer.service;

import me.khudyakov.staticanalyzer.entity.syntaxtree.SyntaxTree;
import me.khudyakov.staticanalyzer.entity.ProgramCode;
import me.khudyakov.staticanalyzer.util.SyntaxAnalyzerException;

public interface SyntaxAnalyzer {

    SyntaxTree createSyntaxTree(ProgramCode programCode) throws SyntaxAnalyzerException;
}
