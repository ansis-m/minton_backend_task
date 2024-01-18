package com.example.mintos.backend.models.requests;

import com.example.mintos.backend.enums.Currency;
import com.example.mintos.backend.utils.CurrencyDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

@Data
public class DepositRequestDto {

    private Long accountId;
    private Double amount;
    @JsonDeserialize(using = CurrencyDeserializer.class)
    private Currency currency;

}
