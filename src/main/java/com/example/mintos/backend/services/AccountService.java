package com.example.mintos.backend.services;

import com.example.mintos.backend.entities.Account;
import com.example.mintos.backend.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    @Autowired
    AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account saveAccount(Account account) {
        return accountRepository.save(account);
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public Account getAccountById(Long id) {
        return accountRepository.findById(id).orElse(null);
    }

    public void deleteAccount(Long id) {
        accountRepository.deleteById(id);
    }

    public Account addFunds(Long accountId, Double amount) {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new RuntimeException(String.format("Account with id %d not found!", accountId)));
        account.setAmount(account.getAmount() + amount);
        return accountRepository.save(account);
    }
}