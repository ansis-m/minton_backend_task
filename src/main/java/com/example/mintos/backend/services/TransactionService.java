package com.example.mintos.backend.services;

import com.example.mintos.backend.entities.Account;
import com.example.mintos.backend.entities.Transaction;
import com.example.mintos.backend.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    @Autowired
    TransactionService(TransactionRepository transactionRepository){
        this.transactionRepository = transactionRepository;
        
    }
    public Transaction createTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public Transaction getTransactionById(Long id) {
        return transactionRepository.findById(id).orElse(null);
    }

    public void deleteTransaction(Long id) {
        transactionRepository.deleteById(id);
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
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setConversionRate(1.00);
        transactionRepository.save(transaction);
    }
}
