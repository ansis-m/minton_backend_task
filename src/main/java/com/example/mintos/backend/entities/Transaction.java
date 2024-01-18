package com.example.mintos.backend.entities;

import com.example.mintos.backend.enums.Currency;
import com.example.mintos.backend.utils.CurrencyConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
    private Account accountFrom;

    @ManyToOne
    @JoinColumn(name = "account_to")
    private Account accountTo;

    @Column(nullable = false)
    private Double amountFrom;

    @Column(nullable = false)
    private Double amountTo;

    @Column
    private Double conversionRate;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "currency", nullable = false)
    @Convert(converter = CurrencyConverter.class)
    private Currency currency;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
