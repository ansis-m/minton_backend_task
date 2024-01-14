package com.example.mintos.backend.controllers;

import com.example.mintos.backend.entities.Account;
import com.example.mintos.backend.entities.Transaction;
import com.example.mintos.backend.services.AccountService;
import com.example.mintos.backend.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/account")
public class AccountController {


    private ClientService clientService;
    private AccountService accountService;

    @Autowired
    AccountController(ClientService clientService, AccountService accountService){
        this.clientService = clientService;
        this.accountService = accountService;
    }

    @PostMapping("/add")
    public ResponseEntity<Account> createAccount(@RequestParam Long clientId, @RequestParam String currency) {
        Account savedAccount = clientService.createAccount(clientId, currency);
        return ResponseEntity.ok(savedAccount);
    }


    @PostMapping("/addfunds")
    public ResponseEntity<Account> addFunds(@RequestParam Long accountId, @RequestParam Double amount) {
        Account account = accountService.addFunds(accountId, amount);
        return ResponseEntity.ok(account);
    }

    @PostMapping("/transactions")
    public ResponseEntity<Page<Transaction>> getTransactions(@RequestParam Long accountId, @RequestParam Integer limit, @RequestParam Integer offset) {
        Page<Transaction> transactions = accountService.getTransactions(accountId, limit, offset);
        return ResponseEntity.ok(transactions);
    }
}