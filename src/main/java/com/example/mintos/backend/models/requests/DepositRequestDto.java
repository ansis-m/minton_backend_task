package com.example.mintos.backend.models.requests;

import com.example.mintos.backend.enums.Currency;
import com.example.mintos.backend.utils.CurrencyDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Deposit and withdrawal of funds request")
public class DepositRequestDto {

    @Schema(description ="Id of the account")
    @NotNull
    @Min(1)
    private Long id;

    @Schema(description ="Amount of the deposit/withdrawal (negative for withdrawal)")
    @NotNull
    private Double amount;

    @Schema(description ="Currency of the deposit/withdrawal. The amount will be converted into the currency of the account.")
    @JsonDeserialize(using = CurrencyDeserializer.class)
    @NotNull
    private Currency currency;

}
