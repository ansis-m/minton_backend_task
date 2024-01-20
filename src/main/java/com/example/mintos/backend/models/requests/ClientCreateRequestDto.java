package com.example.mintos.backend.models.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Create client request body")
public class ClientCreateRequestDto {

    private String name;

}
