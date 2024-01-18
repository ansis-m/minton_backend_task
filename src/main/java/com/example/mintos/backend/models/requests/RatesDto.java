package com.example.mintos.backend.models.requests;

import lombok.Data;

import java.util.HashMap;

@Data
public class RatesDto {
    private HashMap<String, Double> data;
}
