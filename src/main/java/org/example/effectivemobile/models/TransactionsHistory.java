package org.example.effectivemobile.models;


import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name="transactions_history")
public class TransactionsHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", referencedColumnName = "id")
    private Users sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id", referencedColumnName = "id")
    private Users recipient;

    @Column(name = "amount", nullable = false)
    private double amount;

    @Column(name = "transaction_date", nullable = false)
    private Date transactionDate;
}
