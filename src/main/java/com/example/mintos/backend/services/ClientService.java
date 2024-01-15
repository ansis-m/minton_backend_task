package com.example.mintos.backend.services;

import com.example.mintos.backend.entities.Account;
import com.example.mintos.backend.entities.Client;
import com.example.mintos.backend.enums.Currency;
import com.example.mintos.backend.repositories.AccountRepository;
import com.example.mintos.backend.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final AccountRepository accountRepository;

    @Autowired
    ClientService(ClientRepository clientRepository, AccountRepository accountRepository){
        this.clientRepository = clientRepository;
        this.accountRepository = accountRepository;
    }

    public Account createAccount(Long clientId, String currency) {
        if (!Currency.contains(currency.toUpperCase().trim())){
            throw new RuntimeException(String.format("Currency %s not supported", currency));
        };
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found with id " + clientId));

        Account newAccount = new Account();
        newAccount.setClient(client);
        newAccount.setCurrency(currency.toUpperCase().trim());
        newAccount.setAmount(0.0);
        return accountRepository.save(newAccount);
    }
}
