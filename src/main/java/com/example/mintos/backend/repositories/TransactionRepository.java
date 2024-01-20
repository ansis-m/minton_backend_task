package com.example.mintos.backend.repositories;

import com.example.mintos.backend.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
@RepositoryRestResource(exported = false) // remove annotation to expose crud REST endpoints
public interface TransactionRepository extends JpaRepository<Transaction, Long>, JpaSpecificationExecutor<Transaction> {

    @Query(value = "SELECT * FROM transaction WHERE account_from = :accountId OR account_to = "
                   + ":accountId ORDER BY created_at DESC LIMIT :limit OFFSET :offset", nativeQuery = true)
    List<Transaction> findTransactionsWithOffsetAndLimit(Long accountId, Integer limit, Integer offset);

}