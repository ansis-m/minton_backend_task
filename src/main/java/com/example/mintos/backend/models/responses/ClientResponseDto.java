package com.example.mintos.backend.models.responses;

import lombok.Data;

import java.util.Set;

@Data
public class ClientResponseDto {

    private Long id;
    private String name;
    private Set<AccountResponseDto> accounts;

}
