package org.example.effectivemobile.repositories;

import org.example.effectivemobile.models.TransactionsHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionsHistoryRepository extends JpaRepository<TransactionsHistory, Integer> {

}
