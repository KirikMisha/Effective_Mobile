package org.example.effectivemobile.service;

import org.example.effectivemobile.models.BankAccounts;
import org.example.effectivemobile.models.TransactionsHistory;
import org.example.effectivemobile.models.Users;
import org.example.effectivemobile.repositories.BankAccountsRepository;
import org.example.effectivemobile.repositories.TransactionsHistoryRepository;
import org.example.effectivemobile.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class TransferService {

    @Autowired
    private BankAccountsRepository bankAccountsRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private TransactionsHistoryRepository transactionsHistoryRepository;

    @Transactional
    public void transferMoney(Long id, String recipientPhone, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }

        Users sender = usersRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Sender not found"));
        Users recipient = usersRepository.findByPhoneNumber(recipientPhone)
                .orElseThrow(() -> new IllegalArgumentException("Recipient not found"));
        if(sender == recipient){
            throw new IllegalArgumentException("Sender and recipient are the same");
        }

        BankAccounts senderAccount = bankAccountsRepository.findById(sender.getId())
                .orElseThrow(() -> new IllegalArgumentException("Sender account not found"));
        BankAccounts recipientAccount = bankAccountsRepository.findById(recipient.getId())
                .orElseThrow(() -> new IllegalArgumentException("Recipient account not found"));

        synchronized (this) {
            if (senderAccount.getInitialBalance() < amount) {
                throw new IllegalArgumentException("Insufficient funds");
            }

            senderAccount.setInitialBalance(senderAccount.getInitialBalance() - amount);
            recipientAccount.setInitialBalance(recipientAccount.getInitialBalance() + amount);

            bankAccountsRepository.save(senderAccount);
            bankAccountsRepository.save(recipientAccount);
        }

        TransactionsHistory transaction = new TransactionsHistory();
        transaction.setSender(sender);
        transaction.setRecipient(recipient);
        transaction.setAmount(amount);
        transaction.setTransactionDate(new Date());

        transactionsHistoryRepository.save(transaction);
    }
}
