package com.example.mintos.backend.enums;

import com.example.mintos.backend.exceptions.CurrencyNotSupportedException;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CurrencyTest {

    @Test
    void getCurrencyByValidCode() {
        assertEquals(Currency.EUR, Currency.getCurrency("EUR"));
        assertEquals(Currency.USD, Currency.getCurrency("USD"));
    }

    @Test
    void getCurrencyByValidCodeWithWhitespace() {
        assertEquals(Currency.SEK, Currency.getCurrency(" SEK "));
        assertEquals(Currency.GBP, Currency.getCurrency(" GBP "));
    }

    @Test
    void getCurrencyByValidCodeWithWhitespaceInLowerCase() {
        assertEquals(Currency.SEK, Currency.getCurrency(" sek "));
        assertEquals(Currency.GBP, Currency.getCurrency(" gbp "));
    }

    @Test
    void getCurrencyByValidName() {
        assertEquals(Currency.EUR, Currency.getCurrency("Euro"));
        assertEquals(Currency.USD, Currency.getCurrency("US Dollar"));
    }

    @Test
    void getCurrencyByInvalidCode() {
        assertThrows(CurrencyNotSupportedException.class, () -> Currency.getCurrency("XYZ"));
    }

    @Test
    void getCurrencyCodeToNameMapContainsAllCurrencies() {
        Map<String, String> currencyMap = Currency.getCurrencyCodeToNameMap();
        for (Currency currency : Currency.values()) {
            assertTrue(currencyMap.containsKey(currency.getCurrencyCode()));
            assertEquals(currency.getCurrencyName(), currencyMap.get(currency.getCurrencyCode()));
        }
    }

    @Test
    void verifyEnumValues() {
        assertEquals("Euro", Currency.EUR.getCurrencyName());
        assertEquals("EUR", Currency.EUR.getCurrencyCode());
        assertEquals(1.09, Currency.EUR.getExchangeRateToUSD());
    }
}
