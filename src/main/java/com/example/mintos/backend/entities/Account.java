package com.example.mintos.backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;

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
    private String currency;

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
