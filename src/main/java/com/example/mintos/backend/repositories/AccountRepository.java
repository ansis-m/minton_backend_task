package com.example.mintos.backend.repositories;

import com.example.mintos.backend.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

@Repository
@RepositoryRestResource(collectionResourceRel = "account", path = "account")
@CrossOrigin
public interface AccountRepository extends JpaRepository<Account, Long>, JpaSpecificationExecutor<Account> {
}