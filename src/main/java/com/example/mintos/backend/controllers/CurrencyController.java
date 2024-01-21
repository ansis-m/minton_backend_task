package com.example.mintos.backend.controllers;

import com.example.mintos.backend.services.CurrencyExchangeService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/currencies")
public class CurrencyController {

    @GetMapping()
    @Operation(summary = "Returns all supported currencies as a map of 3 letter code and full name.")
    ResponseEntity<Map<String, String>> getCurrencies() {
        return ResponseEntity.ok(CurrencyExchangeService.getCurrencies());
    }
}
