package com.example.mintos.backend.controllers;

import com.example.mintos.backend.services.CurrencyExchangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/currencies")
public class CurrencyController {

    private final CurrencyExchangeService currencyService;

    @Autowired
    CurrencyController(CurrencyExchangeService currencyService) {
        this.currencyService = currencyService;
    }

    @GetMapping("")
    Map<String, String> getCurrencies() {
        return this.currencyService.getCurrencies();
    }


}
