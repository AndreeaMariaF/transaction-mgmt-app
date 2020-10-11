package org.example.transactionsapp.service;

import org.example.transactionsapp.exception.AccountNotFoundException;
import org.example.transactionsapp.model.Account;
import org.example.transactionsapp.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

    public void deleteAccount(long id) {
        accountRepository.findById(id).orElseThrow(AccountNotFoundException::new);
        accountRepository.deleteById(id);
    }

    public void saveAll(List<Account> accounts) {
        accountRepository.saveAll(accounts);
    }

    public Account findByOwnerName(String ownerName) {
        return accountRepository.findByOwnerName(ownerName);
    }
}