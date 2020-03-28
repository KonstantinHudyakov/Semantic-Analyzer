package me.khudyakov.semanticanalyzer.util;

public class ExpressionExecutionException extends Exception {

    public ExpressionExecutionException() {
    }

    public ExpressionExecutionException(String message) {
        super(message);
    }

    public ExpressionExecutionException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExpressionExecutionException(Throwable cause) {
        super(cause);
    }

    public ExpressionExecutionException(String message, Throwable cause, boolean enableSuppression,
                                        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
