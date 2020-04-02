package me.khudyakov.staticanalyzer.util;

public class ExpressionConverterException extends Exception {

    public ExpressionConverterException() {
    }

    public ExpressionConverterException(String message) {
        super(message);
    }

    public ExpressionConverterException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExpressionConverterException(Throwable cause) {
        super(cause);
    }

    public ExpressionConverterException(String message, Throwable cause, boolean enableSuppression,
                                        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
