package com.example.mintos.backend.repositories;

import com.example.mintos.backend.entities.Account;
import com.example.mintos.backend.enums.Currency;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long>, JpaSpecificationExecutor<Account> {
    Page<Account> findByCurrency(Currency currency, Pageable pageable);
    @Query("select a from Account a where a.client.clientId = ?1")
    Page<Account> findByClientId(Long clientId, Pageable pageable);

    @Query("select a from Account a where a.client.clientId = ?1 and a.currency = ?2")
    Page<Account> findByClientIdAndCurrency(Long clientId, Currency currency, Pageable pageable);
}