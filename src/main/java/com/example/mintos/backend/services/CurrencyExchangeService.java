package com.example.mintos.backend.services;

import com.example.mintos.backend.enums.Currency;
import com.example.mintos.backend.models.requests.RatesDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Service
public class CurrencyExchangeService {

    private static final RestTemplate restTemplate = new RestTemplate();
    private static final String API_URL = "https://api.freecurrencyapi.com/v1/latest";
    private static final String API_FAIL_MESSAGE = "Exchange api failed with status code: ";
    private static final String API_RETURN_NULL = "Exchange api returned null.";

    @Value("${API_KEY}")
    private String API_KEY;


    public Double getRate(Currency targetCurrency, Currency sourceCurrency) {

        if (targetCurrency.equals(sourceCurrency)) {
            return 1.00000;
        }
        try {
            ResponseEntity<RatesDto> rates = getCurrencyExchangeRates(targetCurrency, sourceCurrency);
            if (!rates.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException(API_FAIL_MESSAGE + rates.getStatusCode());
            }
            Double result = rates.getBody().getData().get(sourceCurrency.getCurrencyCode());
            if (result != null) {
                return result;
            }
            throw new RuntimeException(API_RETURN_NULL);
        } catch (Exception e) {
            e.printStackTrace();
            return getDefaultRate(targetCurrency, sourceCurrency);
        }
    }

    private static Double getDefaultRate(Currency base, Currency target) {
        double baseCurrencyToUSD = base.getExchangeRateToUSD();
        double targetCurrencyToUSD = target.getExchangeRateToUSD();
        return baseCurrencyToUSD / targetCurrencyToUSD;
    }

    private ResponseEntity<RatesDto> getCurrencyExchangeRates(Currency base, Currency target) {

        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(API_URL)
                .queryParam("apikey", API_KEY)
                .queryParam("base_currency", base.getCurrencyCode())
                .queryParam("currencies", target.getCurrencyCode());

        return restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                null,
                RatesDto.class
        );
    }

    public static Map<String, String> getCurrencies() {
        return Currency.getCurrencyCodeToNameMap();
    }
}
