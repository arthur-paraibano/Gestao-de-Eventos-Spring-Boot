package com.ingressos.api.infra.exception;

import lombok.AllArgsConstructor;
import lombok.ToString;

/**
 * @RestErrorMessage responsável por objetos de resposta em APIs.
 */
@ToString
@AllArgsConstructor
public class RestMessage {

    public RestMessage(int par, String message1) {
    }

    private Integer code;
    private String message;

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
