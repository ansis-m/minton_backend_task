package com.example.mintos.backend.controllers;

import com.example.mintos.backend.models.requests.AccountCreateRequestDto;
import com.example.mintos.backend.models.requests.AccountGetRequestDto;
import com.example.mintos.backend.models.requests.DepositRequestDto;
import com.example.mintos.backend.models.requests.TransferRequestDto;
import com.example.mintos.backend.models.responses.AccountResponseDto;
import com.example.mintos.backend.models.responses.TransactionResponseDto;
import com.example.mintos.backend.services.AccountService;
import com.example.mintos.backend.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/account")
public class AccountController {


    private final ClientService clientService;
    private final AccountService accountService;

    @Autowired
    AccountController(ClientService clientService, AccountService accountService) {
        this.clientService = clientService;
        this.accountService = accountService;
    }

    @PostMapping("")
    public ResponseEntity<Page<AccountResponseDto>> getAccounts(@RequestBody AccountGetRequestDto request) {
        return ResponseEntity.ok(accountService.getAccounts(request));
    }


    @PostMapping("/create")
    public ResponseEntity<AccountResponseDto> createAccount(@RequestBody AccountCreateRequestDto accountCreateRequestDto) {
        AccountResponseDto savedAccount = clientService.createAccount(accountCreateRequestDto);
        return ResponseEntity.ok(savedAccount);
    }

    @PostMapping("/add")
    public ResponseEntity<AccountResponseDto> addFunds(@RequestBody DepositRequestDto depositRequestDto) {
        AccountResponseDto account = accountService.addFunds(depositRequestDto);
        return ResponseEntity.ok(account);
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<TransactionResponseDto>> getTransactions(@RequestParam Long accountId,
                                                                        @RequestParam Integer limit,
                                                                        @RequestParam Integer offset) {
        List<TransactionResponseDto>
                transactions =
                accountService.getTransactions(accountId, limit, offset);
        return ResponseEntity.ok(transactions);
    }

    @PostMapping("/transfer")
    public ResponseEntity<TransactionResponseDto> transfer(@RequestBody TransferRequestDto transferRequestDto) {
        TransactionResponseDto transaction = accountService.transfer(transferRequestDto);
        return ResponseEntity.ok(transaction);
    }
}