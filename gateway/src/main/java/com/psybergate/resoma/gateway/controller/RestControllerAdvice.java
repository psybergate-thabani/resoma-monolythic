package com.psybergate.resoma.gateway.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ValidationException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice(annotations = {RestController.class})
public class RestControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler({
            ValidationException.class,
            ObjectOptimisticLockingFailureException.class
    })
    public ResponseEntity<Object> handleValidationException(Exception ex, WebRequest request) {
        HttpHeaders headers = new HttpHeaders();

        if (ex instanceof ValidationException) {
            HttpStatus status = HttpStatus.BAD_REQUEST;
            ValidationException validationException = (ValidationException) ex;
            return handleException(validationException, headers, status, request);
        } else if (ex instanceof ObjectOptimisticLockingFailureException) {
            HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
            ObjectOptimisticLockingFailureException optimisticLockingFailureException = (ObjectOptimisticLockingFailureException) ex;
            return handleException(optimisticLockingFailureException, headers, status, request);
        } else {
            HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
            return handleException(ex, headers, status, request);
        }
    }

    private ResponseEntity<Object> handleException(Exception ex,
                                                   HttpHeaders headers,
                                                   HttpStatus status,
                                                   WebRequest request) {

        HashMap<String, String> validationMap = new HashMap<>();
        validationMap.put("message", ex.getMessage());
        return handleExceptionInternal(ex, validationMap, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult()
                .getAllErrors()
                .forEach((error) -> {
                    String fieldName = ((FieldError) error).getField();
                    String errorMessage = error.getDefaultMessage();
                    errors.put(fieldName, errorMessage);
                });
        return handleExceptionInternal(ex, errors, headers, status, request);
    }
}
