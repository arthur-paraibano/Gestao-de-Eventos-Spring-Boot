package com.ingressos.api.infra.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.ingressos.api.exceptions.ExistException;
import com.ingressos.api.exceptions.IncorrectParameterException;
import com.ingressos.api.exceptions.InternalServer;
import com.ingressos.api.exceptions.InvalidInput;
import com.ingressos.api.exceptions.IsNullException;
import com.ingressos.api.exceptions.TokenException;

/**
 * @ControllerAdvice é responsável pelos construtores e por iniciar os métodos.
 *                   @IncorrectException, @IsNullException e @ExistException são
 *                   métodos de exceção personalizados que recebem
 *                   o @ExceptionHandler como a classe principal.
 */
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(IncorrectParameterException.class)
    private ResponseEntity<RestMessage> IncorrectException(IncorrectParameterException exception) {
        RestMessage response = new RestMessage(400, exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(IsNullException.class)
    private ResponseEntity<RestMessage> IsNullException(IsNullException exception) {
        RestMessage response = new RestMessage(404, exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(ExistException.class)
    private ResponseEntity<RestMessage> ExistException(ExistException exception) {
        RestMessage response = new RestMessage(409, exception.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(InvalidInput.class)
    private ResponseEntity<RestMessage> BadRequest(InvalidInput exception) {
        RestMessage response = new RestMessage(400, exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(InternalServer.class)
    private ResponseEntity<RestMessage> InternalServer(InternalServer exception) {
        RestMessage response = new RestMessage(500, exception.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(TokenException.class)
    private ResponseEntity<RestMessage> TokenException(TokenException exception) {
        RestMessage response = new RestMessage(301, exception.getMessage());
        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).body(response);
    }
}