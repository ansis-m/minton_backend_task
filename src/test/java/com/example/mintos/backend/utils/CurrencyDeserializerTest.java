package com.example.mintos.backend.utils;

import com.example.mintos.backend.enums.Currency;
import com.example.mintos.backend.exceptions.CurrencyNotSupportedException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CurrencyDeserializerTest {

    private ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Currency.class, new CurrencyDeserializer());
        mapper.registerModule(module);
    }

    @Test
    void deserializeValidCurrencyCode() throws JsonProcessingException {
        String json = "\"USD\"";
        Currency currency = mapper.readValue(json, Currency.class);

        assertEquals(Currency.USD, currency);
    }

    @Test
    void deserializeValidCurrencyCodeWithWhitespace() throws JsonProcessingException {
        String json = "\" EUR \"";
        Currency currency = mapper.readValue(json, Currency.class);

        assertEquals(Currency.EUR, currency);
    }

    @Test
    void deserializeValidCurrencyFullNameWithWhitespace() throws JsonProcessingException {
        String json = "\" Indonesian Rupiah \"";
        Currency currency = mapper.readValue(json, Currency.class);

        assertEquals(Currency.IDR, currency);
    }

    @Test
    void deserializeInvalidCurrencyCode() {
        String json = "\"XYZ\"";

        assertThrows(CurrencyNotSupportedException.class, () -> {
            mapper.readValue(json, Currency.class);
        });
    }

}
