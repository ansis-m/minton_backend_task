package com.example.mintos.backend.controllers;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

import com.example.mintos.backend.enums.Currency;
import com.example.mintos.backend.models.requests.AccountGetRequestDto;
import com.example.mintos.backend.models.responses.AccountResponseDto;
import com.example.mintos.backend.models.responses.ClientResponseDto;
import com.example.mintos.backend.services.AccountService;
import com.example.mintos.backend.services.ClientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class AccountControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AccountService accountService;

    @Mock
    private ClientService clientService;

    @InjectMocks
    private AccountController accountController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = standaloneSetup(accountController).build();
    }

    @Test
    public void testGetAccounts() throws Exception {
        AccountGetRequestDto requestDto = new AccountGetRequestDto() {{ setId(1L); }};
        Page<AccountResponseDto> mockPage = getAccountPage();

        when(accountService.getAccounts(any(AccountGetRequestDto.class))).thenReturn(mockPage);

        mockMvc.perform(post("/account")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(requestDto)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.content", hasSize(1)))
               .andExpect(jsonPath("$.content[0].id", is(1)))
               .andExpect(jsonPath("$.content[0].currency", is("AUD")))
               .andDo(print());

        verify(accountService, times(1)).getAccounts(any(AccountGetRequestDto.class));
    }

    @Test
    public void testCreateAccount() throws Exception {
        // Similar structure as above
    }

    private Page<AccountResponseDto> getAccountPage() {
        List<AccountResponseDto> list = new ArrayList<>();
        list.add(new AccountResponseDto(){{
            setId(1L);
            setAmount(200.0);
            setCurrency(Currency.AUD);
        }});
        return new SerializablePageMock<>(list);
    }
}
