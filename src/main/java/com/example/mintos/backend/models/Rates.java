package com.example.mintos.backend.models;

import lombok.Data;

import java.util.HashMap;

@Data
public class Rates{
    private HashMap<String, Double> data;
}
