package org.example.transactionsapp.repository;

import org.example.transactionsapp.model.Account;
import org.example.transactionsapp.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("select t from Transaction t where t.transactionDate >= :date and t.account.accountNumber = :accountNumber")
    List<Transaction> findAllByTimeframe(@Param("date") Instant date, @Param("accountNumber") String accountNumber);

    List<Transaction> findByAccount(Account account);

    @Query("select t from Transaction t where t.account.accountNumber = :accountNumber")
    List<Transaction> findByAccountNumber(@Param("accountNumber") String accountNumber);
}
