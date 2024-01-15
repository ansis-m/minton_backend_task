package com.example.mintos.backend.services;

import com.example.mintos.backend.entities.Account;
import com.example.mintos.backend.entities.Transaction;
import com.example.mintos.backend.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    @Autowired
    TransactionService(TransactionRepository transactionRepository){
        this.transactionRepository = transactionRepository;
        
    }

    public void createTransaction(Account account, Double amount) {
        Transaction transaction = new Transaction();
        if (amount > 0) {
            transaction.setAccountTo(account);
            transaction.setAmountTo(amount);
        } else {
            transaction.setAccountFrom(account);
            transaction.setAmountFrom(amount);
        }
        transaction.setConversionRate(1.00);
        transactionRepository.save(transaction);
    }

    public Transaction createTransaction(Account target, Account source, Double amount, Double targetAmount, Double exchangeRate) {
        Transaction transaction = new Transaction();
        transaction.setAccountFrom(source);
        transaction.setAccountTo(target);
        transaction.setConversionRate(exchangeRate);
        transaction.setAmountFrom(amount * -1);
        transaction.setAmountTo(targetAmount);
        return transactionRepository.save(transaction);
    }
}
