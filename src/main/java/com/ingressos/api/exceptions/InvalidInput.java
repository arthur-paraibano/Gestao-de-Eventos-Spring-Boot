package com.ingressos.api.exceptions;

public class InvalidInput extends RuntimeException {

    public InvalidInput() {
        super();
    }

    public InvalidInput(String message) {
        super(message);
    }
}
