package com.example.mintos.backend.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDateTime;


@Entity
@Table(name = "transaction")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    @ManyToOne
    @JoinColumn(name = "account_from")
    @JsonBackReference
    private Account accountFrom;

    @ManyToOne
    @JoinColumn(name = "account_to")
    @JsonBackReference
    private Account accountTo;

    @Column(nullable = false)
    private Double amount;

    @Column
    private Double conversionRate;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

}
