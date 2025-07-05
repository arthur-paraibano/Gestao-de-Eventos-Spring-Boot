package com.ingressos.api.exceptions;

public class InternalServer extends RuntimeException {

    public InternalServer() {
        super();
    }

    public InternalServer(String message) {
        super(message);
    }
}
