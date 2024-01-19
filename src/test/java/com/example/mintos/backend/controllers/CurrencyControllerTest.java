package com.example.mintos.backend.controllers;

import com.example.mintos.backend.services.CurrencyExchangeService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import org.mockito.MockedStatic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CurrencyController.class)
public class CurrencyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getCurrencies() throws Exception {
        Map<String, String> mockCurrencies = new HashMap<>();
        mockCurrencies.put("USD", "US Dollar");
        mockCurrencies.put("EUR", "Euro");

        try(MockedStatic<CurrencyExchangeService> mockedStatic = Mockito.mockStatic(CurrencyExchangeService.class)){
            mockedStatic.when(CurrencyExchangeService::getCurrencies).thenReturn(mockCurrencies);

            mockMvc.perform(get("/currencies")
                                    .contentType(MediaType.APPLICATION_JSON))
                   .andExpect(status().isOk())
                   .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                   .andExpect(jsonPath("$.USD").value("US Dollar"))
                   .andExpect(jsonPath("$.EUR").value("Euro"));
        }
    }
}
