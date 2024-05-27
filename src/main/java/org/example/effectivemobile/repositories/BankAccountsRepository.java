package org.example.effectivemobile.repositories;

import org.example.effectivemobile.models.BankAccounts;
import org.example.effectivemobile.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountsRepository extends JpaRepository<BankAccounts, Long> {
    BankAccounts findByUser(Users user);
}
