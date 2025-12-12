package com.twaszak.payments.exceptions;

public class NoTransactionPresent extends Exception {
    public NoTransactionPresent(String message) {
        super(message);
    }
    public NoTransactionPresent(){}
}
