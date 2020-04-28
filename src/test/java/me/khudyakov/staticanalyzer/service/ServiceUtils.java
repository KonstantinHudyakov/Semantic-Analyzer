package me.khudyakov.staticanalyzer.service;

import me.khudyakov.staticanalyzer.entity.ProgramCode;
import me.khudyakov.staticanalyzer.entity.syntaxtree.SyntaxTree;
import me.khudyakov.staticanalyzer.util.SyntaxAnalyzerException;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

class ServiceUtils {

    static final CodeParser codeParser = new CodeParserImpl();
    static final SyntaxAnalyzer syntaxAnalyzer = new SyntaxAnalyzerImpl();
    static final FeatureFinder framingIfFinder = new FramingIfFinder();

    static SyntaxTree parseAndAnalyze(String code) throws SyntaxAnalyzerException, ParseException {
        ProgramCode programCode = codeParser.parse(code);
        return syntaxAnalyzer.createSyntaxTree(programCode);
    }

    static SyntaxTree addChange(SyntaxTreeCache cache, String code) throws ParseException, SyntaxAnalyzerException {
        SyntaxTree syntaxTree = parseAndAnalyze(code);
        cache.addNewSyntaxTreeVersion(syntaxTree);

        return syntaxTree;
    }

    static List<SyntaxTree> addChangeSequence(SyntaxTreeCache cache, String... codes) throws SyntaxAnalyzerException, ParseException {
        List<SyntaxTree> list = new ArrayList<>();
        for(String code : codes) {
            list.add(addChange(cache, code));
        }
        return list;
    }

    static List<SyntaxTree> addChangeSequenceNotThrow(SyntaxTreeCache cache, String... codes) {
        List<SyntaxTree> list = new ArrayList<>();
        for(String code : codes) {
            try {
                list.add(addChange(cache, code));
            } catch (ParseException | SyntaxAnalyzerException ex) {
                // do nothing
            }
        }
        return list;
    }

    static void analyzeOrThrow(String... codes) throws SyntaxAnalyzerException, ParseException {
        for (String code : codes) {
            parseAndAnalyze(code);
        }
    }
}
