package com.example.mintos.backend.models.requests;

import com.example.mintos.backend.enums.Currency;
import com.example.mintos.backend.utils.CurrencyDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Get all accounts for particular client")
public class AccountGetRequestDto {

    @Schema(description ="Id of the client")
    @NotNull
    private Long id;
    @Schema(description ="Optional currency filter")
    @JsonDeserialize(using = CurrencyDeserializer.class)
    private Currency currency;

    @Schema(description ="Page index starting from 0, default is 20")
    private Integer page;

    @Schema(description ="Size of the page, default is 20")
    private Integer size;

}
