package com.example.mintos.backend.models.requests;

import com.example.mintos.backend.enums.Currency;
import com.example.mintos.backend.utils.CurrencyDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Deposit and withdrawal of funds request")
public class DepositRequestDto {

    @Schema(description ="Id of the account")
    private Long id;

    @Schema(description ="Amount of the deposit/withdrawal (negative for withdrawal)")
    private Double amount;

    @Schema(description ="Currency of the deposit/withdrawal. The amount will be converted into the currency of the account.")
    @JsonDeserialize(using = CurrencyDeserializer.class)
    private Currency currency;

}
