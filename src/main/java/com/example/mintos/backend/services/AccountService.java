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

import static com.example.mintos.backend.utils.StaticHelpers.getPageable;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {

    private static final String SOURCE404 = "Source account not found";
    private static final String TARGET404 = "Target account not found";
    private static final String ACCOUNT404 = "Account not found!";
    private static final String NO_FUNDS = "Not enough funds in the source account";
    private static final String CURRENCIES_NOT_MATCHING = "Target account is in %s, does not accept %s";

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final TransactionService transactionService;
    private final ResponseMapper responseMapper;
    private final CurrencyExchangeService exchangeService;

    @Autowired
    AccountService(
            AccountRepository accountRepository,
            TransactionRepository transactionRepository,
            TransactionService transactionService,
            ResponseMapper responseMapper,
            CurrencyExchangeService exchangeService
    ) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.transactionService = transactionService;
        this.responseMapper = responseMapper;
        this.exchangeService = exchangeService;
    }

    public Page<AccountResponseDto> getAccounts(AccountGetRequestDto request) {
        Pageable pageable = getPageable(request.getPage(), request.getSize());
        if (request.getCurrency() != null) {
            return accountRepository
                    .findByClientIdAndCurrency(request.getId(), request.getCurrency(), pageable)
                    .map(responseMapper::map);
        }
        return accountRepository
                .findByClientId(request.getId(), pageable)
                .map(responseMapper::map);
    }

    @Transactional
    public AccountResponseDto addFunds(DepositRequestDto depositRequestDto) {

        Account account = accountRepository
                .findById(depositRequestDto.getId())
                .orElseThrow(() -> new AccountNotFoundException(ACCOUNT404, depositRequestDto.getId()));

        Double rate = exchangeService.getRate(depositRequestDto.getCurrency(), account.getCurrency());

        account.setAmount(account.getAmount() + depositRequestDto.getAmount() * rate);
        account = accountRepository.saveAndFlush(account);
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
                .orElseThrow(() -> new AccountNotFoundException(SOURCE404, transferRequestDto.getSourceId()));

        Account target = accountRepository
                .findById(transferRequestDto.getTargetId())
                .orElseThrow(() -> new AccountNotFoundException(TARGET404, transferRequestDto.getTargetId()));

        checkTargetCurrency(Currency.getCurrency(transferRequestDto.getCurrency()), target);

        Double exchangeRate = exchangeService.getRate(target.getCurrency(), source.getCurrency());
        Double sourceAmount = transferRequestDto.getAmount() * exchangeRate;

        target.setAmount(target.getAmount() + transferRequestDto.getAmount());
        source.setAmount(source.getAmount() - sourceAmount);
        checkBalance(source);

        accountRepository.saveAndFlush(source);
        accountRepository.saveAndFlush(target);
        return responseMapper.map(transactionService.createTransaction(target, source, transferRequestDto, exchangeRate));
    }

    private void checkTargetCurrency(Currency currency, Account target) {
        if (!currency.equals(target.getCurrency())) {
            throw new CurrenciesNotMatchingException(
                    String.format(CURRENCIES_NOT_MATCHING, target.getCurrency(), currency)
            );
        }
    }

    private void checkBalance(Account source) {
        if (source.getAmount() < 0) {
            throw new RuntimeException(NO_FUNDS);
        }
    }
}