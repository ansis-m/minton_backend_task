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

@Repository
@RepositoryRestResource(exported = false) // remove annotation to expose REST crud endpoints
public interface AccountRepository extends JpaRepository<Account, Long>, JpaSpecificationExecutor<Account> {

    @Query("select a from Account a where a.client.id = ?1")
    Page<Account> findByClientId(Long clientId, Pageable pageable);

    @Query("select a from Account a where a.client.id = ?1 and a.currency = ?2")
    Page<Account> findByClientIdAndCurrency(Long clientId, Currency currency, Pageable pageable);
}
