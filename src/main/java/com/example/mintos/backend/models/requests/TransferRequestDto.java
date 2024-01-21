package com.example.mintos.backend.models.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Transfer between the accounts request")
public class TransferRequestDto {

    @Schema(description ="Id of the account to which the funds will be added")
    private Long targetId;

    @Schema(description ="Id of the account from which the funds will be withdrawn")
    private Long sourceId;
    @Schema(description ="Amount of the transfer")
    @NotNull
    private Double amount;

    @Schema(description ="Currency of the transfer has to match the currency of the target account")
    @NotNull
    @NotBlank
    private String currency;

}
