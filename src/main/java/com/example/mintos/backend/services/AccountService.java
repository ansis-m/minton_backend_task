package com.example.mintos.backend.services;

import com.example.mintos.backend.entities.Account;
import com.example.mintos.backend.entities.Transaction;
import com.example.mintos.backend.models.Transfer;
import com.example.mintos.backend.repositories.AccountRepository;
import com.example.mintos.backend.repositories.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final TransactionService transactionService;
    private final CurrencyExchangeService exchangeService;

    @Autowired
    AccountService(
            AccountRepository accountRepository,
            TransactionRepository transactionRepository,
            TransactionService transactionService,
            CurrencyExchangeService exchangeService
    ) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.transactionService = transactionService;
        this.exchangeService = exchangeService;
    }

    @Transactional
    public Account addFunds(Long accountId, Double amount) {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new RuntimeException(String.format("Account with id %d not found!", accountId)));
        account.setAmount(account.getAmount() + amount);
        account = accountRepository.save(account);
        transactionService.createTransaction(account, amount);
        return account;
    }

    public List<Transaction> getTransactions(Long accountId, Integer limit, Integer offset) {
        return  transactionRepository.findTransactionsWithOffsetAndLimit(accountId, limit, offset);
    }

    @Transactional
    public Transaction transfer(Transfer transfer) {

        Account target = accountRepository.findById(transfer.getTargetId()).orElseThrow(() -> new RuntimeException("Target account not found"));
        Account source = accountRepository.findById(transfer.getSourceId()).orElseThrow(() -> new RuntimeException("Target account not found"));
        checkBalance(source, transfer);
        Double exchangeRate = exchangeService.getRate(target.getCurrency(), source.getCurrency());
        Double targetAmount = target.getAmount() + transfer.getAmount() * exchangeRate;

        source.setAmount(source.getAmount() - transfer.getAmount());
        target.setAmount(targetAmount);
        accountRepository.save(source);
        accountRepository.save(target);
        return transactionService.createTransaction(target, source, transfer.getAmount(), transfer.getAmount() * exchangeRate, exchangeRate);
    }

    private void checkBalance(Account source, Transfer transfer) {
        if (transfer.getAmount() > source.getAmount()) {
            throw new RuntimeException("Not enough funds in the source account");
        }
    }
}