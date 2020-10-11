package org.example.transactionsapp.controller;

import org.example.transactionsapp.model.Account;
import org.example.transactionsapp.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    AccountService accountService;

    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity<Account> createAccount(@Valid @RequestBody Account account) {
        return new ResponseEntity<>(accountService.createAccount(account), HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity<Account> deleteAccount(@PathVariable long id) {
        accountService.deleteAccount(id);
        return new ResponseEntity("Account and its transactions were removed" , HttpStatus.OK);
    }
}
