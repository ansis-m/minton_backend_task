package com.example.mintos.backend.controllers;

import com.example.mintos.backend.models.requests.AccountCreateRequestDto;
import com.example.mintos.backend.models.requests.AccountGetRequestDto;
import com.example.mintos.backend.models.requests.DepositRequestDto;
import com.example.mintos.backend.models.requests.TransferRequestDto;
import com.example.mintos.backend.models.responses.AccountResponseDto;
import com.example.mintos.backend.models.responses.TransactionResponseDto;
import com.example.mintos.backend.services.AccountService;
import com.example.mintos.backend.services.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
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
    @Operation(summary = "Gets all accounts for a particular user (id),"
                         + " if page or size is null first page with 20 results is returned.")
    public ResponseEntity<Page<AccountResponseDto>> getAccounts(@RequestBody AccountGetRequestDto request) {
        return ResponseEntity.ok(accountService.getAccounts(request));
    }

    @PostMapping("/create")
    @Operation(summary = "Creates an account for a particular user (id).")
    public ResponseEntity<AccountResponseDto> createAccount(@RequestBody AccountCreateRequestDto accountCreateRequestDto) {
        AccountResponseDto savedAccount = clientService.createAccount(accountCreateRequestDto);
        return ResponseEntity.ok(savedAccount);
    }

    @PostMapping("/add")
    @Operation(summary = "Add or withdraw funds to a particular account (id)."
                         + " Amount is converted to/from the currency of the particular account.")
    public ResponseEntity<AccountResponseDto> addFunds(@RequestBody DepositRequestDto depositRequestDto) {
        AccountResponseDto account = accountService.addFunds(depositRequestDto);
        return ResponseEntity.ok(account);
    }

    @GetMapping("/transactions")
    @Operation(summary = "Get transactions for a particular account (id)."
                         + " Results provided with mandatory limit and offset.")
    public ResponseEntity<List<TransactionResponseDto>> getTransactions(@RequestParam @Min(1) Long accountId,
                                                                        @RequestParam @Min(0) Integer limit,
                                                                        @RequestParam @Min(0) Integer offset)
    {
        List<TransactionResponseDto> transactions = accountService.getTransactions(accountId, limit, offset);
        return ResponseEntity.ok(transactions);
    }

    @PostMapping("/transfer")
    @Operation(summary = "Transfer funds between the accounts."
                         + " Funds are withdrawn from the source account (sourceId) and deposited into a target account (targetId).")
    public ResponseEntity<TransactionResponseDto> transfer(@RequestBody TransferRequestDto transferRequestDto) {
        TransactionResponseDto transaction = accountService.transfer(transferRequestDto);
        return ResponseEntity.ok(transaction);
    }
}