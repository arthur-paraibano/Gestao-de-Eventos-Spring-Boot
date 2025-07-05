package com.ingressos.api.exceptions;

public class TokenException extends RuntimeException {

    public TokenException() {
        super();
    }

    public TokenException(String message) {
        super(message);
    }
}
