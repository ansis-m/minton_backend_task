package com.example.mintos.backend.models.responses;

import com.example.mintos.backend.enums.Currency;
import lombok.Data;


@Data
public class AccountResponseDto {

    private Long accountId;
    private Currency currency;
    private Double amount;

}
