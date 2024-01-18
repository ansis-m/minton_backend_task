package com.example.mintos.backend.exceptions;

import lombok.Getter;

@Getter
public class AccountNotFoundException extends RuntimeException {

    private final Long id;

    public AccountNotFoundException(String message, Long id) {
        super(message);
        this.id = id;
    }
}
