package me.khudyakov.semanticanalyzer.util;

public class SyntaxAnalyzerException extends Exception {

    public SyntaxAnalyzerException() {
    }

    public SyntaxAnalyzerException(String message) {
        super(message);
    }

    public SyntaxAnalyzerException(String message, Throwable cause) {
        super(message, cause);
    }

    public SyntaxAnalyzerException(Throwable cause) {
        super(cause);
    }

    public SyntaxAnalyzerException(String message, Throwable cause, boolean enableSuppression,
                                   boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
