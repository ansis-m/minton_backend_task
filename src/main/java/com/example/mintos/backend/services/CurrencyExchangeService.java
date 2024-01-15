package com.example.mintos.backend.services;

import org.springframework.stereotype.Service;

@Service
public class CurrencyExchangeService {
    public Double getRate(String targetCurrency, String sourceCurrency) {
        if(targetCurrency.equals(sourceCurrency)) {
            return 1.00000;
        }
        //ToDo
        return 1.0;
    }
}
