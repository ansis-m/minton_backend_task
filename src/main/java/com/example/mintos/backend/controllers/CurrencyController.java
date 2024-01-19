package com.example.mintos.backend.controllers;

import com.example.mintos.backend.services.CurrencyExchangeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/currencies")
public class CurrencyController {

    @GetMapping()
    ResponseEntity<Map<String, String>> getCurrencies() {
        return ResponseEntity.ok(CurrencyExchangeService.getCurrencies());
    }
}
