package com.example.mintos.backend.models.requests;

import com.example.mintos.backend.enums.Currency;
import com.example.mintos.backend.utils.CurrencyDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

@Data
public class AccountGetRequestDto {

    private Long clientId;
    @JsonDeserialize(using = CurrencyDeserializer.class)
    private Currency currency;
    private Integer page;
    private Integer size;

}
