package com.example.mintos.backend.models.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Create client request body")
public class ClientCreateRequestDto {

    @NotNull
    @NotBlank
    private String name;

}
