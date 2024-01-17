package com.example.mintos.backend.controllers;

import com.example.mintos.backend.entities.Account;
import com.example.mintos.backend.entities.Transaction;
import com.example.mintos.backend.models.Transfer;
import com.example.mintos.backend.services.AccountService;
import com.example.mintos.backend.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/account")
public class AccountController {


    private final ClientService clientService;
    private final AccountService accountService;

    @Autowired
    AccountController(ClientService clientService, AccountService accountService){
        this.clientService = clientService;
        this.accountService = accountService;
    }

    @PostMapping("/create/account")
    public ResponseEntity<Account> createAccount(@RequestParam Long clientId, @RequestParam String currency) {
        Account savedAccount = clientService.createAccount(clientId, currency);
        return ResponseEntity.ok(savedAccount);
    }


    @PostMapping("/add/funds")
    public ResponseEntity<Account> addFunds(@RequestParam Long accountId, @RequestParam Double amount) {
        Account account = accountService.addFunds(accountId, amount);
        return ResponseEntity.ok(account);
    }

    @PostMapping("/transactions")
    public ResponseEntity<List<Transaction>> getTransactions(@RequestParam Long accountId, @RequestParam Integer limit, @RequestParam Integer offset) {
        List<Transaction> transactions = accountService.getTransactions(accountId, limit, offset);
        return ResponseEntity.ok(transactions);
    }

    @PostMapping("/transfer")
    public ResponseEntity<Transaction> transfer(@RequestBody Transfer transfer) {
        Transaction transaction = accountService.transfer(transfer);
        return ResponseEntity.ok(transaction);
    }
}