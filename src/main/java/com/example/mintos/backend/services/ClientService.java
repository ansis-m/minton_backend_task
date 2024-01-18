package com.example.mintos.backend.services;

import com.example.mintos.backend.entities.Account;
import com.example.mintos.backend.entities.Client;
import com.example.mintos.backend.mappers.ResponseMapper;
import com.example.mintos.backend.models.requests.AccountCreateRequestDto;
import com.example.mintos.backend.models.responses.AccountResponseDto;
import com.example.mintos.backend.models.responses.ClientResponseDto;
import com.example.mintos.backend.repositories.AccountRepository;
import com.example.mintos.backend.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final AccountRepository accountRepository;
    private final ResponseMapper responseMapper;

    @Autowired
    ClientService(
            ClientRepository clientRepository,
            AccountRepository accountRepository,
            ResponseMapper responseMapper
    ) {
        this.clientRepository = clientRepository;
        this.accountRepository = accountRepository;
        this.responseMapper = responseMapper;
    }

    public AccountResponseDto createAccount(AccountCreateRequestDto accountCreateRequestDto) {

        Client client = clientRepository.findById(accountCreateRequestDto.getClientId())
                                        .orElseThrow(() -> new RuntimeException(
                                                "Client not found with id "
                                                + accountCreateRequestDto.getClientId()));

        Account newAccount = new Account();
        newAccount.setClient(client);
        newAccount.setCurrency(accountCreateRequestDto.getCurrency());
        newAccount.setAmount(0.0);
        return responseMapper.map(accountRepository.save(newAccount));
    }

    public Page<ClientResponseDto> getClients(Integer page, Integer size) {

        Pageable pageable;
        if(page != null && size!= null) {
            pageable = PageRequest.of(page, size);
        } else {
            pageable = PageRequest.of(0, 20);
        }
        return clientRepository.findAll(pageable).map(responseMapper::map);

    }
}
