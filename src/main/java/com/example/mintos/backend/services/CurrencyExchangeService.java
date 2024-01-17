package com.example.mintos.backend.services;

import com.example.mintos.backend.enums.Currency;
import com.example.mintos.backend.models.Rates;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

@Service
public class CurrencyExchangeService {


    private final RestTemplate restTemplate = new RestTemplate();
    private final String API_URL = "https://api.freecurrencyapi.com/v1/latest";
    private final String API_KEY = "fca_live_A3k7IjJEm4g5IOLaqB2e9VWIBVM5HRAhv1J2hxP0";


    public Double getRate(String targetCurrency, String sourceCurrency) {


        if(targetCurrency.equals(sourceCurrency)) {
            return 1.00000;
        }
        try {
            ResponseEntity<Rates> rates = getCurrencyExchangeRates(sourceCurrency, targetCurrency);
            if (!rates.getStatusCode().is2xxSuccessful()){
                throw new RuntimeException("Exchange api failed");
            }
            return rates.getBody().getData().get(targetCurrency);
        } catch (Exception e) {
            e.printStackTrace();
            return getDefaultRate(sourceCurrency, targetCurrency);
        }

    }

    private Double getDefaultRate(String sourceCurrency, String targetCurrency) {
        try{
            double baseCurrencyToUSD = Currency.getCurrency(sourceCurrency).getExchangeRateToUSD();
            double targetCurrencyToUSD = Currency.getCurrency(targetCurrency).getExchangeRateToUSD();
            return baseCurrencyToUSD / targetCurrencyToUSD;
        } catch (NullPointerException e) {
            throw new RuntimeException("Transfer failed because currency is not supported");
        }
    }

    public ResponseEntity<Rates> getCurrencyExchangeRates(String base, String target) {

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(API_URL)
                .queryParam("apikey", API_KEY)
                .queryParam("base_currency", base)
                .queryParam("currencies", target);

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        return restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                null,
                Rates.class
        );
    }

    public Map<String, String> getCurrencies() {
        return Currency.getCurrencyCodeToNameMap();
    }
}
