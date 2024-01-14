package com.example.mintos.backend.entities;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
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
    @JsonBackReference
    private Client client;

    @Column(nullable = false)
    private String currency;

    @Column(nullable = false)
    private Double amount;

    @OneToMany(mappedBy = "accountFrom")
    @ToString.Exclude
    @JsonManagedReference
    private Set<Transaction> transactionsFrom;

    @OneToMany(mappedBy = "accountTo")
    @ToString.Exclude
    @JsonManagedReference
    private Set<Transaction> transactionsTo;
}
