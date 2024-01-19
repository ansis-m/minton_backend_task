package com.example.mintos.backend.models.responses;

import com.example.mintos.backend.entities.Account;
import com.example.mintos.backend.enums.Currency;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TransactionResponseDto {

    private Long id;
    private Account accountFrom;
    private Account accountTo;
    private Double amountFrom;
    private Double amountTo;
    private Double conversionRate;
    private LocalDateTime createdAt;
    private Currency currency;

}
