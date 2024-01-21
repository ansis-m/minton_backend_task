package com.example.mintos.backend.mappers;

import com.example.mintos.backend.entities.Account;
import com.example.mintos.backend.entities.Client;
import com.example.mintos.backend.entities.Transaction;
import com.example.mintos.backend.models.responses.AccountResponseDto;
import com.example.mintos.backend.models.responses.ClientResponseDto;
import com.example.mintos.backend.models.responses.TransactionResponseDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ResponseMapper {

    TransactionResponseDto map(Transaction transaction);

    AccountResponseDto map(Account account);

    ClientResponseDto map(Client client);

}
