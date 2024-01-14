package com.example.mintos.backend.entities;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "account")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Column(nullable = false)
    private String currency;

    @Column(nullable = false)
    private Double amount;

    @OneToMany(mappedBy = "accountFrom")
    @ToString.Exclude
    private Set<Transaction> transactionsFrom;

    @OneToMany(mappedBy = "accountTo")
    @ToString.Exclude
    private Set<Transaction> transactionsTo;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Account account = (Account) o;
        return getAccountId() != null && Objects.equals(getAccountId(), account.getAccountId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
