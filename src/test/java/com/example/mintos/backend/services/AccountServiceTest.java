package com.example.mintos.backend.services;

import com.example.mintos.backend.controllers.SerializablePageMock;
import com.example.mintos.backend.entities.Account;
import com.example.mintos.backend.entities.Transaction;
import com.example.mintos.backend.enums.Currency;
import com.example.mintos.backend.exceptions.AccountNotFoundException;
import com.example.mintos.backend.mappers.ResponseMapper;
import com.example.mintos.backend.models.requests.AccountGetRequestDto;
import com.example.mintos.backend.models.requests.DepositRequestDto;
import com.example.mintos.backend.models.requests.TransferRequestDto;
import com.example.mintos.backend.models.responses.AccountResponseDto;
import com.example.mintos.backend.models.responses.TransactionResponseDto;
import com.example.mintos.backend.repositories.AccountRepository;
import com.example.mintos.backend.repositories.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private ResponseMapper responseMapper;

    @Mock
    private CurrencyExchangeService exchangeService;

    @Mock
    private TransactionService transactionService;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private AccountService accountService;

    @Captor
    ArgumentCaptor<Account> accountCaptor;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void testGetAccountsWithCurrency() {
        AccountGetRequestDto request = new AccountGetRequestDto() {{
            setId(1L);
            setCurrency(Currency.USD);
            setPage(0);
            setSize(10);
        }};

        Account mockAccount = new Account();
        Page<Account> accountsPage = new SerializablePageMock<>(Collections.singletonList(mockAccount));
        when(accountRepository.findByClientIdAndCurrency(eq(1L), eq(Currency.USD), any(Pageable.class)))
                .thenReturn(accountsPage);

        AccountResponseDto mockResponseDto = new AccountResponseDto();
        when(responseMapper.map(mockAccount)).thenReturn(mockResponseDto);

        Page<AccountResponseDto> response = accountService.getAccounts(request);

        assertNotNull(response);
        assertEquals(1, response.getContent().size());
        assertEquals(mockResponseDto, response.getContent().get(0));
        verify(accountRepository, times(1)).findByClientIdAndCurrency(eq(1L), eq(Currency.USD), any(Pageable.class));
        verify(accountRepository, times(0)).findByClientId(eq(1L), any(Pageable.class));
        verify(responseMapper, times(1)).map(accountsPage.getContent().get(0));
        verify(responseMapper, times(accountsPage.getContent().size())).map(any(Account.class));
    }

    @Test
    void testGetAccountsNoCurrency() {
        AccountGetRequestDto request = new AccountGetRequestDto() {{
            setId(1L);
            setPage(0);
            setSize(10);
        }};

        Account mockAccount = new Account();
        Page<Account> accountsPage = new SerializablePageMock<>(Collections.singletonList(mockAccount));
        when(accountRepository.findByClientId(eq(1L), any(Pageable.class)))
                .thenReturn(accountsPage);

        AccountResponseDto mockResponseDto = new AccountResponseDto();
        when(responseMapper.map(mockAccount)).thenReturn(mockResponseDto);

        Page<AccountResponseDto> response = accountService.getAccounts(request);

        assertNotNull(response);
        assertEquals(1, response.getContent().size());
        assertEquals(mockResponseDto, response.getContent().get(0));
        verify(accountRepository, times(0)).findByClientIdAndCurrency(eq(1L), any(Currency.class), any(Pageable.class));
        verify(accountRepository, times(1)).findByClientId(eq(1L), any(Pageable.class));
        verify(responseMapper, times(1)).map(accountsPage.getContent().get(0));
        verify(responseMapper, times(accountsPage.getContent().size())).map(any(Account.class));
    }

    @Test
    void testAddFundsSuccess() {
        Long accountId = 1L;
        Double depositAmount = 100.0;
        Currency depositCurrency = Currency.USD;
        Double exchangeRate = 2.0;
        Double initialAmount = 200.0;
        Account account = new Account() {{
            setId(accountId);
            setAmount(initialAmount);
            setCurrency(Currency.EUR);
        }};

        DepositRequestDto depositRequestDto = new DepositRequestDto() {{
            setId(accountId);
            setAmount(depositAmount);
            setCurrency(depositCurrency);
        }};


        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
        when(accountRepository.saveAndFlush(account)).thenReturn(account);
        when(exchangeService.getRate(depositCurrency, Currency.EUR)).thenReturn(exchangeRate);

        AccountResponseDto expectedResponse = new AccountResponseDto();
        when(responseMapper.map(account)).thenReturn(expectedResponse);

        AccountResponseDto response = accountService.addFunds(depositRequestDto);


        verify(accountRepository).saveAndFlush(accountCaptor.capture());
        Account capturedAccount = accountCaptor.getValue();
        assertNotNull(capturedAccount);
        assertEquals(initialAmount + exchangeRate * depositAmount, capturedAccount.getAmount());

        verify(accountRepository).findById(accountId);
        verify(accountRepository).saveAndFlush(account);
        verify(responseMapper).map(account);
        verify(transactionService).createTransaction(account, depositAmount, depositCurrency, exchangeRate);
        assertEquals(expectedResponse, response);
    }

    @Test
    void testAddFundsAccountNotFound() {
        Long accountId = 1L;
        DepositRequestDto depositRequestDto = new DepositRequestDto();
        depositRequestDto.setId(accountId);
        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> accountService.addFunds(depositRequestDto));
    }

    @Test
    void testAddFundsWithdrawFailure() {
        Long accountId = 1L;
        Double depositAmount = -100.0;
        Currency depositCurrency = Currency.USD;
        Double exchangeRate = 2.0;
        Double initialAmount = 0.0;
        Account account = new Account() {{
            setId(accountId);
            setAmount(initialAmount);
            setCurrency(Currency.EUR);
        }};

        DepositRequestDto depositRequestDto = new DepositRequestDto() {{
            setId(accountId);
            setAmount(depositAmount);
            setCurrency(depositCurrency);
        }};


        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
        when(accountRepository.saveAndFlush(account)).thenReturn(account);
        when(exchangeService.getRate(depositCurrency, Currency.EUR)).thenReturn(exchangeRate);

        AccountResponseDto expectedResponse = new AccountResponseDto();
        when(responseMapper.map(account)).thenReturn(expectedResponse);

        assertThrows(RuntimeException.class, () -> accountService.addFunds(depositRequestDto));
    }

    @Test
    void testGetTransactions() {
        Long accountId = 1L;
        Integer limit = 10;
        Integer offset = 0;

        Transaction mockTransaction = new Transaction();

        TransactionResponseDto mockResponseDto = new TransactionResponseDto();

        when(transactionRepository.findTransactionsWithOffsetAndLimit(accountId, limit, offset)).thenReturn(List.of(mockTransaction));
        when(responseMapper.map(mockTransaction)).thenReturn(mockResponseDto);

        List<TransactionResponseDto> result = accountService.getTransactions(accountId, limit, offset);

        assertNotNull(result);
        verify(transactionRepository, times(1)).findTransactionsWithOffsetAndLimit(accountId, limit, offset);
        verify(responseMapper, times(1)).map(mockTransaction);
        assertFalse(result.isEmpty());
        assertEquals(mockResponseDto, result.get(0));
    }

    @Test
    void testTransferSuccess() {
        Long sourceId = 1L;
        Long targetId = 2L;
        Double amount = 100.0;
        Currency currency = Currency.USD;
        Double exchangeRate = 2.0;
        Double sourceInitialAmount = 200.0;
        Double targetInitialAmount = 150.0;

        Account source = new Account() {{
            setId(sourceId);
            setAmount(sourceInitialAmount);
            setCurrency(Currency.EUR);
        }};


        Account target = new Account() {{
            setId(targetId);
            setAmount(targetInitialAmount);
            setCurrency(Currency.USD);
        }};


        TransferRequestDto transferRequestDto = new TransferRequestDto() {{
            setSourceId(sourceId);
            setTargetId(targetId);
            setAmount(amount);
            setCurrency(currency.toString());
        }};


        when(accountRepository.findById(sourceId)).thenReturn(Optional.of(source));
        when(accountRepository.findById(targetId)).thenReturn(Optional.of(target));
        when(exchangeService.getRate(Currency.USD, Currency.EUR)).thenReturn(exchangeRate);

        TransactionResponseDto expectedResponse = new TransactionResponseDto();
        when(transactionService.createTransaction(target, source, transferRequestDto, exchangeRate))
                .thenReturn(new Transaction());
        when(responseMapper.map(any(Transaction.class))).thenReturn(expectedResponse);

        TransactionResponseDto response = accountService.transfer(transferRequestDto);

        verify(accountRepository, times(2)).saveAndFlush(accountCaptor.capture());
        List<Account> capturedAccounts = accountCaptor.getAllValues();

        Account capturedSource = capturedAccounts.get(0);
        Account capturedTarget = capturedAccounts.get(1);
        assertEquals(sourceInitialAmount - amount * exchangeRate, capturedSource.getAmount());
        assertEquals(targetInitialAmount + amount, capturedTarget.getAmount());

        assertEquals(expectedResponse, response);
        verify(exchangeService).getRate(Currency.USD, Currency.EUR);
        verify(accountRepository).saveAndFlush(source);
        verify(accountRepository).saveAndFlush(target);
    }

    @Test
    void testTransferSourceAccountNotFound() {
        Long sourceId = 1L;
        TransferRequestDto transferRequestDto = new TransferRequestDto();
        transferRequestDto.setSourceId(sourceId);
        when(accountRepository.findById(sourceId)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> accountService.transfer(transferRequestDto));
    }


}