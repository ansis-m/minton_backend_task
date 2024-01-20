package com.example.mintos.backend.services;

import com.example.mintos.backend.entities.Account;
import com.example.mintos.backend.entities.Transaction;
import com.example.mintos.backend.enums.Currency;
import com.example.mintos.backend.models.requests.TransferRequestDto;
import com.example.mintos.backend.repositories.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;

public class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateTransactionSingleAccountPositiveAmount() {

        Account account = new Account();
        Double amount = 100.0;
        Currency currency = Currency.USD;
        Double rate = 1.0;
        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);

        transactionService.createTransaction(account, amount, currency, rate);

        verify(transactionRepository).saveAndFlush(transactionCaptor.capture());
        Transaction capturedTransaction = transactionCaptor.getValue();

        assertNull(capturedTransaction.getAccountFrom());
        assertEquals(account, capturedTransaction.getAccountTo());
        assertEquals(Math.abs(amount * rate), capturedTransaction.getAmountTo());
        assertNull(capturedTransaction.getAmountFrom());
        assertEquals(currency, capturedTransaction.getCurrency());
    }

    @Test
    public void testCreateTransactionSingleAccountNegativeAmount() {

        Account account = new Account();
        Double amount = -100.0;
        Currency currency = Currency.USD;
        Double rate = 1.0;
        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);

        transactionService.createTransaction(account, amount, currency, rate);

        verify(transactionRepository).saveAndFlush(transactionCaptor.capture());
        Transaction capturedTransaction = transactionCaptor.getValue();


        assertNull(capturedTransaction.getAccountTo());
        assertEquals(account, capturedTransaction.getAccountFrom());
        assertEquals(Math.abs(amount * rate), capturedTransaction.getAmountFrom());
        assertNull(capturedTransaction.getAmountTo());
        assertEquals(currency, capturedTransaction.getCurrency());
    }


    @Test
    public void testCreateTransactionTransferBetweenAccounts() {

        Account source = new Account() {{ setId(1L); }};
        Account target = new Account() {{ setId(2L); }};
        TransferRequestDto dto = new TransferRequestDto() {{
            setCurrency("USD");
            setAmount(100.0);
            setSourceId(1L);
            setTargetId(2L);
        }};
        Double exchangeRate = 2.0;

        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);
        transactionService.createTransaction(target, source, dto, exchangeRate);

        verify(transactionRepository).saveAndFlush(transactionCaptor.capture());
        Transaction capturedTransaction = transactionCaptor.getValue();

        assertEquals(exchangeRate, capturedTransaction.getConversionRate());
        assertEquals(source, capturedTransaction.getAccountFrom());
        assertEquals(target, capturedTransaction.getAccountTo());
        assertEquals(Currency.USD, capturedTransaction.getCurrency());
        assertEquals(exchangeRate * 100.0, capturedTransaction.getAmountFrom());
        assertEquals(100.0, capturedTransaction.getAmountTo());
    }
}
