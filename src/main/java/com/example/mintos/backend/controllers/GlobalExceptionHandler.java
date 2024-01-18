package com.example.mintos.backend.controllers;

import com.example.mintos.backend.exceptions.AccountNotFoundException;
import com.example.mintos.backend.exceptions.CurrenciesNotMatchingException;
import com.example.mintos.backend.exceptions.CurrencyNotSupportedException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<Object> accountNotFound(AccountNotFoundException ex, HttpServletRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", ex.getMessage());
        body.put("account id", ex.getId());
        body.put("path", request.getRequestURI());
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CurrencyNotSupportedException.class)
    public ResponseEntity<Object> currencyNotSupported(CurrencyNotSupportedException ex, HttpServletRequest request) {
        return getObjectResponseEntity(ex, request);
    }

    @ExceptionHandler(CurrenciesNotMatchingException.class)
    public ResponseEntity<Object> currenciesNotMatching(CurrenciesNotMatchingException ex, HttpServletRequest request) {
        return getObjectResponseEntity(ex, request);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> runtimeException(RuntimeException ex, HttpServletRequest request) {
        return getObjectResponseEntity(ex, request);
    }

    private ResponseEntity<Object> getObjectResponseEntity(RuntimeException ex, HttpServletRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", ex.getMessage());
        body.put("path", request.getRequestURI());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}
