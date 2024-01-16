package com.example.mintos.backend.services;

import com.example.mintos.backend.models.Rates;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

//https://api.freecurrencyapi.com/v1/latest?apikey=fca_live_A3k7IjJEm4g5IOLaqB2e9VWIBVM5HRAhv1J2hxP0&currencies=EUR%2CUSD%2CCAD&base_currency=CAD

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
            return getCurrencyExchangeRates(sourceCurrency, targetCurrency).getBody().getData().get(targetCurrency);
        } catch (Exception e) {
            return 1.0;
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
}
