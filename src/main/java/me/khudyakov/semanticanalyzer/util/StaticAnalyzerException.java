package me.khudyakov.semanticanalyzer.util;

public class StaticAnalyzerException extends Exception {

    public StaticAnalyzerException() {
    }

    public StaticAnalyzerException(String message) {
        super(message);
    }

    public StaticAnalyzerException(String message, Throwable cause) {
        super(message, cause);
    }

    public StaticAnalyzerException(Throwable cause) {
        super(cause);
    }

    public StaticAnalyzerException(String message, Throwable cause, boolean enableSuppression,
                                   boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
