package org.example.transactionsapp.repository;

import org.example.transactionsapp.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

     Optional<Account> findByAccountNumber(String accountNumber);

     Account findByOwnerName(String ownerName);
}
