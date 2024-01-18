package com.example.mintos.backend.services;

import com.example.mintos.backend.entities.Account;
import com.example.mintos.backend.entities.Transaction;
import com.example.mintos.backend.enums.Currency;
import com.example.mintos.backend.models.requests.TransferRequestDto;
import com.example.mintos.backend.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    @Autowired
    TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public void createTransaction(Account account, Double amount, Currency currency, Double rate) {
        Transaction transaction = new Transaction();
        if (amount > 0) {
            transaction.setAccountTo(account);
            transaction.setAmountTo(Math.abs(amount * rate));
        } else {
            transaction.setAccountFrom(account);
            transaction.setAmountFrom(Math.abs(amount * rate));
        }
        transaction.setConversionRate(rate);
        transaction.setCurrency(currency);
        transactionRepository.save(transaction);
    }

    public Transaction createTransaction(Account target,
                                         Account source,
                                         TransferRequestDto transferRequestDto,
                                         Double exchangeRate) {
        Transaction transaction = new Transaction();
        transaction.setAccountFrom(source);
        transaction.setAccountTo(target);
        transaction.setConversionRate(exchangeRate);
        transaction.setAmountFrom(transferRequestDto.getAmount() * exchangeRate);
        transaction.setAmountTo(transferRequestDto.getAmount());
        transaction.setCurrency(Currency.getCurrency(transferRequestDto.getCurrency()));
        return transactionRepository.save(transaction);
    }
}
