package org.example.transactionsapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class TransactionsNotFoundException extends RuntimeException {

    public TransactionsNotFoundException(){
        super("Transactions not found");
    }
}
