package org.example.effectivemobile.service;

import org.example.effectivemobile.models.BankAccounts;
import org.example.effectivemobile.repositories.BankAccountsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class BalanceUpdateService {

    private static final Logger logger = LoggerFactory.getLogger(BalanceUpdateService.class);

    private final BankAccountsRepository bankAccountsRepository;

    @Autowired
    public BalanceUpdateService(BankAccountsRepository bankAccountsRepository) {
        this.bankAccountsRepository = bankAccountsRepository;
    }

    @Transactional
    @Scheduled(fixedRate = 60000)
    public void updateBalances() {
        logger.info("Starting balance update task");
        List<BankAccounts> accounts = bankAccountsRepository.findAll();
        for (BankAccounts account : accounts) {
            double initialBalance = account.getInitialBalance();
            double currentBalance = account.getInitialBalance();
            double newBalance = currentBalance * 1.05;
            double maxBalance = initialBalance * 2.07;
            if (newBalance > maxBalance) {
                newBalance = maxBalance;
            }
            account.setInitialBalance(newBalance);
            bankAccountsRepository.save(account);
        }
        logger.info("Finished balance update task");
    }
}

