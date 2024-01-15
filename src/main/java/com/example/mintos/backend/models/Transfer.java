package com.example.mintos.backend.models;

import lombok.Data;
import lombok.Getter;

@Data
public class Transfer {

    private Long targetId;
    private Long sourceId;
    private Double amount;
}
