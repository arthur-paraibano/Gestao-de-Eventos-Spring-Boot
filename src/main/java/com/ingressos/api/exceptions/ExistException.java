package com.ingressos.api.exceptions;

public class ExistException extends RuntimeException {

    public ExistException() {
        super();
    }

    public ExistException(String message) {
        super(message);
    }
}
