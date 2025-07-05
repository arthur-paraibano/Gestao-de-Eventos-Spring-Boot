package com.ingressos.api.exceptions;

public class IncorrectParameterException extends RuntimeException {

    public IncorrectParameterException() {
        super();
    }

    public IncorrectParameterException(String message) {
        super(message);
    }
}
