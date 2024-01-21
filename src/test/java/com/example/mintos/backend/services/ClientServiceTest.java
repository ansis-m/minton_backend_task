package com.example.mintos.backend.services;

import com.example.mintos.backend.controllers.SerializablePageMock;
import com.example.mintos.backend.entities.Account;
import com.example.mintos.backend.entities.Client;
import com.example.mintos.backend.enums.Currency;
import com.example.mintos.backend.mappers.ResponseMapper;
import com.example.mintos.backend.models.requests.AccountCreateRequestDto;
import com.example.mintos.backend.models.requests.ClientCreateRequestDto;
import com.example.mintos.backend.models.responses.AccountResponseDto;
import com.example.mintos.backend.models.responses.ClientResponseDto;
import com.example.mintos.backend.repositories.AccountRepository;
import com.example.mintos.backend.repositories.ClientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private ResponseMapper responseMapper;

    @InjectMocks
    private ClientService clientService;

    @Test
    public void testCreateAccountClientExists() {

        AccountCreateRequestDto requestDto = new AccountCreateRequestDto();
        requestDto.setId(1L);
        requestDto.setCurrency(Currency.USD);

        Client client = new Client();
        client.setId(1L);
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

        Account account = new Account();
        account.setClient(client);
        account.setCurrency(Currency.USD);
        account.setAmount(0.0);
        when(accountRepository.saveAndFlush(ArgumentMatchers.<Account>any())).thenReturn(account);


        ArgumentCaptor<Account> transactionCaptor = ArgumentCaptor.forClass(Account.class);

        AccountResponseDto expectedResponse = new AccountResponseDto();
        when(responseMapper.map(account)).thenReturn(expectedResponse);

        AccountResponseDto response = clientService.createAccount(requestDto);


        verify(accountRepository).saveAndFlush(transactionCaptor.capture());
        Account accountTransaction = transactionCaptor.getValue();
        assertEquals(accountTransaction.getClient(), client);
        assertEquals(accountTransaction.getCurrency(), Currency.USD);
        assertEquals(accountTransaction.getAmount(), 0.0);
        assertEquals(expectedResponse, response);
        verify(clientRepository, times(1)).findById(1L);
        verify(accountRepository, times(1)).saveAndFlush(ArgumentMatchers.<Account>any());
    }

    @Test
    public void testCreateAccountClientNotFound() {

        long clientId = 1L;
        AccountCreateRequestDto requestDto = new AccountCreateRequestDto() {{ setId(clientId); }};
        when(clientRepository.findById(clientId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            clientService.createAccount(requestDto);
        });

        String expectedMessage = "Client not found with id " + clientId;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testGetClients() {

        int page = 0;
        int size = 5;
        ArgumentCaptor<PageRequest> transactionCaptor = ArgumentCaptor.forClass(PageRequest.class);
        Client client = new Client();
        Page<Client> clientPage = new SerializablePageMock<>(Collections.singletonList(client));
        when(clientRepository.findAll(ArgumentMatchers.<Pageable>any())).thenReturn(clientPage);

        ClientResponseDto clientResponseDto = new ClientResponseDto();
        when(responseMapper.map(client)).thenReturn(clientResponseDto);

        Page<ClientResponseDto> result = clientService.getClients(page, size);

        verify(clientRepository, times(1)).findAll(any(PageRequest.class));
        verify(responseMapper, times(1)).map(client);
        verify(clientRepository).findAll(transactionCaptor.capture());
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.getContent().size());
        assertEquals(clientResponseDto, result.getContent().get(0));
        PageRequest pageRequestTransaction = transactionCaptor.getValue();
        assertEquals(pageRequestTransaction.getPageSize(), size);
        assertEquals(pageRequestTransaction.getPageNumber(), page);
    }

    @Test
    public void testRegisterClientSuccess() {

        String clientName = "Test Client";
        ClientCreateRequestDto requestDto = new ClientCreateRequestDto() {{ setName(clientName); }};
        Client savedClient = new Client() {{ setName(clientName) ;}};
        when(clientRepository.saveAndFlush(any(Client.class))).thenReturn(savedClient);

        ClientResponseDto expectedResponseDto = new ClientResponseDto();
        expectedResponseDto.setName(clientName);
        when(responseMapper.map(savedClient)).thenReturn(expectedResponseDto);

        ClientResponseDto actualResponseDto = clientService.registerClient(requestDto);

        assertNotNull(actualResponseDto);
        assertEquals(clientName, actualResponseDto.getName());
        verify(clientRepository, times(1)).saveAndFlush(any(Client.class));
        verify(responseMapper, times(1)).map(savedClient);
    }

    @Test
    public void testGetClientSuccess() {

        Long clientId = 1L;
        Client client = new Client();
        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));

        ClientResponseDto expectedResponse = new ClientResponseDto();
        when(responseMapper.map(client)).thenReturn(expectedResponse);

        ClientResponseDto actualResponse = clientService.getClient(clientId);

        assertNotNull(actualResponse);
        assertEquals(expectedResponse, actualResponse);
        verify(clientRepository, times(1)).findById(clientId);
        verify(responseMapper, times(1)).map(client);
    }

    @Test
    public void testGetClientNotFound() {

        Long clientId = 2L;
        when(clientRepository.findById(clientId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> clientService.getClient(clientId));
        verify(clientRepository, times(1)).findById(clientId);
    }

}