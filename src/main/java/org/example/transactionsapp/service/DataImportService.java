package org.example.transactionsapp.service;

import org.example.transactionsapp.model.Account;
import org.example.transactionsapp.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DataImportService {

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionService transactionService;

    public void createTransactions() {
        Account firstAccount = accountService.findByOwnerName("name 1");

        List<Transaction> transactions = new ArrayList<>();
        for(int i =1; i <=5; i++){
            Transaction tr = new Transaction();
            tr.setDescription("transaction description " + i);
            tr.setAmount(1000+i);
            tr.setAccount(firstAccount);
            transactions.add(tr);
        }
        transactionService.saveAll(transactions);
    }

    public void createAccounts() {
        List<Account> accounts = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            Account is = new Account();
            is.setId(i);
            is.setBalance(1000+i);
            is.setOwnerName("name " + i);
            accounts.add(is);
        }
        accountService.saveAll(accounts);
    }
}