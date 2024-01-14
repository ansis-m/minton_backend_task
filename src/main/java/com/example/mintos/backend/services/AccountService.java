package com.example.mintos.backend.services;

import com.example.mintos.backend.entities.Account;
import com.example.mintos.backend.entities.Transaction;
import com.example.mintos.backend.repositories.AccountRepository;
import com.example.mintos.backend.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final TransactionService transactionService;

    @Autowired
    AccountService(AccountRepository accountRepository, TransactionRepository transactionRepository, TransactionService transactionService) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.transactionService = transactionService;
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
        account = accountRepository.save(account);
        transactionService.createTransaction(account, amount);
        return account;
    }

    public Page<Transaction> getTransactions(Long accountId, Integer limit, Integer offset) {
        PageRequest pageRequest = (limit == 0)
                ? PageRequest.of(offset, Integer.MAX_VALUE, Sort.by("createdAt").descending())
                : PageRequest.of(offset, limit, Sort.by("createdAt").descending());

        return  transactionRepository.findByAccountFromAccountIdOrAccountToAccountId(accountId, accountId, pageRequest);

    }
}