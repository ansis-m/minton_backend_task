package com.example.mintos.backend.services;

import com.example.mintos.backend.enums.Currency;
import com.example.mintos.backend.models.requests.RatesDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class CurrencyExchangeServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private CurrencyExchangeService currencyExchangeService;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetRateEqualCurrencies() {
        Currency source = Currency.USD;
        Currency target = Currency.USD;

        double rate = currencyExchangeService.getRate(target, source);

        assertEquals(1.00000, rate, "Rate should be 1 when currencies are the same");
    }


    @Test
    public void testGetRateFailedApiResponse() {

            Currency source = Currency.USD;
            Currency target = Currency.EUR;
            RatesDto ratesDto = new RatesDto();
            HashMap<String, Double> ratesMap = new HashMap<>();
            ratesMap.put(source.getCurrencyCode(), 0.85);
            ratesDto.setData(ratesMap);

            double rate = currencyExchangeService.getRate(target, source);

            assertEquals(Currency.EUR.getDefaultExchangeRateToUSD() / Currency.USD.getDefaultExchangeRateToUSD(), rate, "The rate should match the values in enum");
    }

    @Test
    public void testGetDefaultRate() {
        double rate = currencyExchangeService.getRate(Currency.BRL, Currency.EUR);
        assertEquals(Currency.BRL.getDefaultExchangeRateToUSD() / Currency.EUR.getDefaultExchangeRateToUSD(), rate);
    }

    @Test
    public void testGetCurrencies() {
        Map<String, String> currencies = CurrencyExchangeService.getCurrencies();
        Map<String, String> codeMap = Currency.getCurrencyCodeToNameMap();

        assertNotNull(currencies, "Currencies map should not be null");
        assertFalse(currencies.isEmpty(), "Currencies map should not be empty");

        codeMap.forEach((key, value) -> {
            assertTrue(currencies.containsKey(key));
            Assertions.assertEquals(currencies.get(key), value);
        });
    }
}