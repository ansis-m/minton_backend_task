package com.example.mintos.backend.services;

import com.example.mintos.backend.enums.Currency;
import com.example.mintos.backend.models.requests.RatesDto;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Service
public class CurrencyExchangeService {


    private final RestTemplate restTemplate = new RestTemplate();
    private final String API_URL = "https://api.freecurrencyapi.com/v1/latest";
    private final String API_KEY = "fca_live_A3k7IjJEm4g5IOLaqB2e9VWIBVM5HRAhv1J2hxP0";


    public Double getRate(Currency targetCurrency, Currency sourceCurrency) {
        if (targetCurrency.equals(sourceCurrency)) {
            return 1.00000;
        }
        try {
            ResponseEntity<RatesDto>
                    rates =
                    getCurrencyExchangeRates(targetCurrency, sourceCurrency);
            if (!rates.getStatusCode()
                      .is2xxSuccessful()) {
                throw new RuntimeException("Exchange api failed with status code: "
                                           + rates.getStatusCode());
            }
            if (rates.getBody()
                     .getData()
                     .get(sourceCurrency.getCurrencyCode()) != null) {
                return rates.getBody()
                            .getData()
                            .get(sourceCurrency.getCurrencyCode());
            }
            throw new RuntimeException("Exchange api returned null.");
        } catch (Exception e) {
            e.printStackTrace();
            return getDefaultRate(targetCurrency, sourceCurrency);
        }

    }

    private Double getDefaultRate(Currency base, Currency target) {
        double baseCurrencyToUSD = base.getExchangeRateToUSD();
        double targetCurrencyToUSD = target.getExchangeRateToUSD();
        return baseCurrencyToUSD / targetCurrencyToUSD;
    }

    private ResponseEntity<RatesDto> getCurrencyExchangeRates(Currency base, Currency target) {

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(API_URL)
                                                           .queryParam("apikey", API_KEY)
                                                           .queryParam("base_currency",
                                                                       base.getCurrencyCode())
                                                           .queryParam("currencies",
                                                                       target.getCurrencyCode());

        return restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                null,
                RatesDto.class
        );
    }

    public Map<String, String> getCurrencies() {
        return Currency.getCurrencyCodeToNameMap();
    }
}
