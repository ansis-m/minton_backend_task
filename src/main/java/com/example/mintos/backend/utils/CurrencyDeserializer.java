package com.example.mintos.backend.utils;

import com.example.mintos.backend.enums.Currency;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class CurrencyDeserializer extends StdDeserializer<Currency> {

    public CurrencyDeserializer() {
        super(Currency.class);
    }

    @Override
    public Currency deserialize(JsonParser jsonParser,
                                DeserializationContext context) throws IOException {
        String currencyCode = jsonParser.getText();
        return Currency.getCurrency(currencyCode);
    }
}