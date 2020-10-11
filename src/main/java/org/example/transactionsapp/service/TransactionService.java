package org.example.transactionsapp.service;

import org.example.transactionsapp.dto.TransactionDTO;
import org.example.transactionsapp.exception.AccountNotFoundException;
import org.example.transactionsapp.exception.TransactionsNotFoundException;
import org.example.transactionsapp.model.Account;
import org.example.transactionsapp.model.Transaction;
import org.example.transactionsapp.repository.AccountRepository;
import org.example.transactionsapp.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    public void saveTransaction(TransactionDTO transactionDTO) {
        Transaction transaction = buildTransaction(transactionDTO);
        transactionRepository.save(transaction);
    }

    public List<Transaction> getAll() {
        if(transactionRepository.findAll().isEmpty()){
            throw new TransactionsNotFoundException();
        }
        return transactionRepository.findAll();
    }

    public List<Transaction> getAllByAccount(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber).orElseThrow(AccountNotFoundException::new);
        return transactionRepository.findByAccount(account);
    }

    private Transaction buildTransaction(TransactionDTO transactionDTO) {
        Account account = accountRepository.findByAccountNumber(transactionDTO.getAccountNumber()).orElseThrow(AccountNotFoundException::new);

        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(transactionDTO.getAmount());
        transaction.setDescription(transactionDTO.getDescription());

        return transaction;
    }

    public List<Transaction> getByHours(int hours, String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber).orElseThrow(AccountNotFoundException::new);
        Instant date = Instant.now().minus(Duration.ofHours(hours));
        return transactionRepository.findAllByTimeframe(date,accountNumber);
    }

    public List<Transaction> getByDays(int days, String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber).orElseThrow(AccountNotFoundException::new);
        Instant date = Instant.now().minus(Duration.ofDays(days));
        return transactionRepository.findAllByTimeframe(date,accountNumber);
    }

    public void saveAll(List<Transaction> transactions) {
        transactionRepository.saveAll(transactions);
    }
}