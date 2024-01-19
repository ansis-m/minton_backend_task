package com.example.mintos.backend.models.requests;

import com.example.mintos.backend.enums.Currency;
import com.example.mintos.backend.utils.CurrencyDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

@Data
public class AccountCreateRequestDto {
    private Long clientId;
    @JsonDeserialize(using = CurrencyDeserializer.class)
    private Currency currency;

}
