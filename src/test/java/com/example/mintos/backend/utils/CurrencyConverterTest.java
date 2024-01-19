package com.example.mintos.backend.utils;

import com.example.mintos.backend.enums.Currency;
import com.example.mintos.backend.exceptions.CurrencyNotSupportedException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNull;

class CurrencyConverterTest {

    private final CurrencyConverter converter = new CurrencyConverter();

    @Test
    void convertToDatabaseColumnWithNull() {
        assertNull(converter.convertToDatabaseColumn(null));
    }

    @Test
    void convertToDatabaseColumnWithValidCurrency() {
        assertEquals("EUR", converter.convertToDatabaseColumn(Currency.EUR));
        assertEquals("USD", converter.convertToDatabaseColumn(Currency.USD));
    }

    @Test
    void convertToEntityAttributeWithNull() {
        assertNull(converter.convertToEntityAttribute(null));
    }

    @Test
    void convertToEntityAttributeWithValidCurrencyCode() {
        assertEquals(Currency.EUR, converter.convertToEntityAttribute("EUR"));
        assertEquals(Currency.USD, converter.convertToEntityAttribute("USD"));
    }

    @Test
    void convertToEntityAttributeWithInvalidCurrencyCode() {
        assertThrows(CurrencyNotSupportedException.class, () -> converter.convertToEntityAttribute("XYZ"));
    }
}