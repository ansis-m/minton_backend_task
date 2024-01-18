package com.example.mintos.backend.exceptions;

public class CurrenciesNotMatchingException extends RuntimeException {

    public CurrenciesNotMatchingException(String message) {
        super(message);
    }

}
