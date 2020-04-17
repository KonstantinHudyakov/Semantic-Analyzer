package me.khudyakov.staticanalyzer.service;

import me.khudyakov.staticanalyzer.program.ProgramCode;
import me.khudyakov.staticanalyzer.program.SyntaxTree;
import me.khudyakov.staticanalyzer.util.ExpressionConverterException;
import me.khudyakov.staticanalyzer.util.ExpressionExecutionException;
import me.khudyakov.staticanalyzer.util.SyntaxAnalyzerException;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceUtils {

    static final CodeParser codeParser = new CodeParserImpl();
    static final SyntaxAnalyzer syntaxAnalyzer = new SyntaxAnalyzerImpl();
    static final FeatureFinder framingIfFinder = new FramingIfFeatureFinder();

    static SyntaxTree parseAndAnalyze(String code) throws ExpressionConverterException, ExpressionExecutionException, SyntaxAnalyzerException, ParseException {
        ProgramCode programCode = codeParser.parse(code);
        return syntaxAnalyzer.analyze(programCode);
    }

    static SyntaxTree addChange(SyntaxTreeChangesCache cache, String code) throws ParseException, ExpressionConverterException, ExpressionExecutionException, SyntaxAnalyzerException {
        SyntaxTree syntaxTree = parseAndAnalyze(code);
        cache.addNewChange(syntaxTree);

        return syntaxTree;
    }

    static List<SyntaxTree> addChangeSequence(SyntaxTreeChangesCache cache, String... codes) throws ExpressionConverterException, ExpressionExecutionException, SyntaxAnalyzerException, ParseException {
        List<SyntaxTree> list = new ArrayList<>();
        for(String code : codes) {
            list.add(addChange(cache, code));
        }

        return list;
    }

    static void analyzeOrThrow(String... codes) throws ExpressionConverterException, ExpressionExecutionException, SyntaxAnalyzerException, ParseException {
        for (String code : codes) {
            parseAndAnalyze(code);
        }
    }

    static void analyzeAndCatchExceptions(Class<? extends Throwable> expectedException, String... codes) {
        Arrays.stream(codes)
              .forEach(code -> assertThrows(expectedException, () -> parseAndAnalyze(code)));
    }
}
