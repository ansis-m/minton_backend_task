package com.example.mintos.backend.services;

import com.example.mintos.backend.entities.Account;
import com.example.mintos.backend.enums.Currency;
import com.example.mintos.backend.exceptions.AccountNotFoundException;
import com.example.mintos.backend.exceptions.CurrenciesNotMatchingException;
import com.example.mintos.backend.mappers.ResponseMapper;
import com.example.mintos.backend.models.requests.AccountGetRequestDto;
import com.example.mintos.backend.models.requests.DepositRequestDto;
import com.example.mintos.backend.models.requests.TransferRequestDto;
import com.example.mintos.backend.models.responses.AccountResponseDto;
import com.example.mintos.backend.models.responses.TransactionResponseDto;
import com.example.mintos.backend.repositories.AccountRepository;
import com.example.mintos.backend.repositories.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final TransactionService transactionService;
    private final CurrencyExchangeService exchangeService;
    private final ResponseMapper responseMapper;

    @Autowired
    AccountService(
            AccountRepository accountRepository,
            TransactionRepository transactionRepository,
            TransactionService transactionService,
            CurrencyExchangeService exchangeService,
            ResponseMapper responseMapper
    ) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.transactionService = transactionService;
        this.exchangeService = exchangeService;
        this.responseMapper = responseMapper;
    }

    public Page<AccountResponseDto> getAccounts(AccountGetRequestDto request) {
        Pageable pageable;
        if(request.getPage() != null && request.getSize()!= null) {
            pageable = PageRequest.of(request.getPage(), request.getSize());
        } else {
            pageable = PageRequest.of(0, 20);
        }
        if (request.getCurrency() != null) {
            return accountRepository.findByClientIdAndCurrency(request.getClientId(), request.getCurrency(), pageable).map(responseMapper::map);
        }
        return accountRepository.findByClientId(request.getClientId(), pageable).map(responseMapper::map);
    }

    @Transactional
    public AccountResponseDto addFunds(DepositRequestDto depositRequestDto) {

        Account account = accountRepository
                .findById(depositRequestDto.getAccountId())
                .orElseThrow(() -> new AccountNotFoundException(String.format("Account with id %d not found!", depositRequestDto.getAccountId()), depositRequestDto.getAccountId()));

        Double rate = exchangeService.getRate(depositRequestDto.getCurrency(), account.getCurrency());

        account.setAmount(account.getAmount() + depositRequestDto.getAmount() * rate);
        account = accountRepository.save(account);
        transactionService.createTransaction(account, depositRequestDto.getAmount(), depositRequestDto.getCurrency(), rate);
        return responseMapper.map(account);
    }

    public List<TransactionResponseDto> getTransactions(Long accountId, Integer limit, Integer offset) {
        return responseMapper.map(transactionRepository.findTransactionsWithOffsetAndLimit(accountId, limit, offset));
    }

    @Transactional
    public TransactionResponseDto transfer(TransferRequestDto transferRequestDto) {

        Account source = accountRepository
                .findById(transferRequestDto.getSourceId())
                .orElseThrow(() -> new AccountNotFoundException("Source account not found", transferRequestDto.getSourceId()));

        Account target = accountRepository
                .findById(transferRequestDto.getTargetId())
                .orElseThrow(() -> new AccountNotFoundException("Target account not found", transferRequestDto.getTargetId()));

        checkTargetCurrency(Currency.getCurrency(transferRequestDto.getCurrency()), target);

        Double exchangeRate = exchangeService.getRate(target.getCurrency(), source.getCurrency());
        Double sourceAmount = transferRequestDto.getAmount() * exchangeRate;

        source.setAmount(source.getAmount() - sourceAmount);
        checkBalance(source);
        target.setAmount(target.getAmount() + transferRequestDto.getAmount());
        accountRepository.save(source);
        accountRepository.save(target);
        return responseMapper
                .map(transactionService.createTransaction(target, source, transferRequestDto, exchangeRate));
    }

    private void checkTargetCurrency(Currency currency, Account target) {
        if (!currency.equals(target.getCurrency())) {
            throw new CurrenciesNotMatchingException(
                    String.format("Target account is in %s, does not accept %s", target.getCurrency(), currency)
            );
        }
    }

    private void checkBalance(Account source) {
        if (source.getAmount() < 0) {
            throw new RuntimeException("Not enough funds in the source account");
        }
    }
}