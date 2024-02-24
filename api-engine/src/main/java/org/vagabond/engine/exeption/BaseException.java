package org.vagabond.engine.exeption;

public abstract class BaseException extends RuntimeException {

    protected BaseException() {
    }

    protected BaseException(String message) {
        super(message);
    }

    protected BaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
