package com.example.mintos.backend.services;

import com.example.mintos.backend.entities.Account;
import com.example.mintos.backend.entities.Transaction;
import com.example.mintos.backend.enums.Currency;
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
    public Account addFunds(Long accountId, Double amount, String currency) {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new RuntimeException(String.format("Account with id %d not found!", accountId)));
        Double rate = exchangeService.getRate(currency, account.getCurrency());

        account.setAmount(account.getAmount() + amount * rate);
        account = accountRepository.save(account);
        transactionService.createTransaction(account, Math.abs(amount), currency, rate);
        return account;
    }

    public List<Transaction> getTransactions(Long accountId, Integer limit, Integer offset) {
        return  transactionRepository.findTransactionsWithOffsetAndLimit(accountId, limit, offset);
    }

    @Transactional
    public Transaction transfer(Transfer transfer) {

        System.out.println("currency: " + transfer.getCurrency());
        Account source = accountRepository.findById(transfer.getSourceId()).orElseThrow(() -> new RuntimeException("Source account not found"));
        Account target = accountRepository.findById(transfer.getTargetId()).orElseThrow(() -> new RuntimeException("Target account not found"));
        checkTargetCurrency(transfer.getCurrency(), target);
        Double exchangeRate = exchangeService.getRate(target.getCurrency(), source.getCurrency());
        Double sourceAmount = transfer.getAmount() * exchangeRate;
        source.setAmount(source.getAmount() - sourceAmount);
        checkBalance(source);
        target.setAmount(target.getAmount() + transfer.getAmount());
        accountRepository.save(source);
        accountRepository.save(target);
        return transactionService.createTransaction(target, source, transfer, exchangeRate);
    }

    private void checkTargetCurrency(String currency, Account target) {
        //todo change the antity to contain instance of enum
        if(!Currency.getCurrency(currency).equals(Currency.getCurrency(target.getCurrency()))) {
            throw new RuntimeException(String.format("Target account is in %s, does not accept %s", target.getCurrency(), currency));
        }
    }

    private void checkBalance(Account source) {
        if (source.getAmount() < 0) {
            throw new RuntimeException("Not enough funds in the source account");
        }
    }
}