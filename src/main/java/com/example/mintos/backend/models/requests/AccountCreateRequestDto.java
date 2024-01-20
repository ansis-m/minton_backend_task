package com.example.mintos.backend.models.requests;

import com.example.mintos.backend.enums.Currency;
import com.example.mintos.backend.utils.CurrencyDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Create account request")
public class AccountCreateRequestDto {
    @Schema(description ="Client id for which the account is to be registered")
    private Long id;

    @Schema(description ="Currency of the new account")
    @JsonDeserialize(using = CurrencyDeserializer.class)
    private Currency currency;

}
