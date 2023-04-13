package com.hubpay.exception;

public class TransactionBadRequest extends RuntimeException {

    public TransactionBadRequest(String msg) {
        super(msg);
    }
}
