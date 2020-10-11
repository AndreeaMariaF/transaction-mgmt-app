package org.example.transactionsapp.controller;

import org.example.transactionsapp.dto.TransactionDTO;
import org.example.transactionsapp.model.Transaction;
import org.example.transactionsapp.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public void addTransaction(@Valid @RequestBody TransactionDTO transactionDTO) {
         transactionService.saveTransaction(transactionDTO);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Transaction>> getTransactions() {
        return new ResponseEntity<>(transactionService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/account/{accountNumber}")
    public ResponseEntity<List<Transaction>> getTransactionsByAccount(@PathVariable String accountNumber) {
        return new ResponseEntity<>(transactionService.getAllByAccount(accountNumber), HttpStatus.OK);
    }

    @GetMapping("/hours/{hours}")
    public ResponseEntity<List<Transaction>> getByHours(@PathVariable(name = "hours") int hours,
                                                        @RequestHeader(name="accountNumber") String accountNumber) {
        return new ResponseEntity<>(transactionService.getByHours(hours,accountNumber),HttpStatus.OK);
    }

    @GetMapping("/days/{days}")
    public ResponseEntity<List<Transaction>> getByDays(@PathVariable(name = "days") int days,
                                                       @RequestHeader(name="accountNumber") String accountNumber) {
        return new ResponseEntity<>(transactionService.getByDays(days,accountNumber),HttpStatus.OK);
    }
}