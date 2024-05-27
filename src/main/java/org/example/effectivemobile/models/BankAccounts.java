package org.example.effectivemobile.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "bank_accounts")
public class BankAccounts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "initial_balance", nullable = false)
    private double initialBalance;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", referencedColumnName = "id")
    private Users user;
}
