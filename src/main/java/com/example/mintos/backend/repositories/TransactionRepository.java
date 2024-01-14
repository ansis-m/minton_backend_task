package com.example.mintos.backend.repositories;

import com.example.mintos.backend.entities.Account;
import com.example.mintos.backend.entities.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

@Repository
@RepositoryRestResource(collectionResourceRel = "transaction", path = "transaction")
@CrossOrigin
public interface TransactionRepository extends JpaRepository<Transaction, Long>, JpaSpecificationExecutor<Transaction> {
    Page<Transaction> findByAccountFromAccountIdOrAccountToAccountId(Long accountFromId, Long accountToId, Pageable pageable);
}