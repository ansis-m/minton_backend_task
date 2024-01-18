package com.example.mintos.backend.entities;

import com.example.mintos.backend.enums.Currency;
import com.example.mintos.backend.utils.CurrencyConverter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
    @JsonIgnore
    private Client client;

    @Column(nullable = false)
    @Convert(converter = CurrencyConverter.class)
    private Currency currency;

    @Column(nullable = false)
    @Min(0)
    private Double amount;

    @OneToMany(mappedBy = "accountFrom")
    @ToString.Exclude
    @JsonIgnore
    private Set<Transaction> transactionsFrom;

    @OneToMany(mappedBy = "accountTo")
    @ToString.Exclude
    @JsonIgnore
    private Set<Transaction> transactionsTo;
}
