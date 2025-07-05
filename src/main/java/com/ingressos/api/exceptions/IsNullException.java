package com.ingressos.api.exceptions;

public class IsNullException extends RuntimeException {

    public IsNullException() {
        super();
    }

    public IsNullException(String message) {
        super(message);
    }
}
