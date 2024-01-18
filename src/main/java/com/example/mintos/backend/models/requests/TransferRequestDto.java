package com.example.mintos.backend.models.requests;

import lombok.Data;

@Data
public class TransferRequestDto {

    private Long targetId;
    private Long sourceId;
    private Double amount;
    private String currency;
}
