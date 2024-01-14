package com.example.mintos.backend.controllers;

import com.example.mintos.backend.entities.Account;
import com.example.mintos.backend.entities.Client;
import com.example.mintos.backend.enums.Currency;
import com.example.mintos.backend.repositories.AccountRepository;
import com.example.mintos.backend.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    @PostMapping("/add")
    public ResponseEntity<Account> createAccount(@RequestParam Long clientId, @RequestParam String currency) {

        if (!Currency.contains(currency.toUpperCase().trim())){
            throw new RuntimeException(String.format("Currency %s not supported", currency));
        };
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found with id " + clientId));

        Account newAccount = new Account();
        newAccount.setClient(client);
        newAccount.setCurrency(currency.toUpperCase().trim());
        newAccount.setAmount(0.0);
        Account savedAccount = accountRepository.save(newAccount);
        return ResponseEntity.ok(savedAccount);
    }
}