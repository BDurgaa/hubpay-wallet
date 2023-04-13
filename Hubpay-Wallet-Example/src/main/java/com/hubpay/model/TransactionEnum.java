package com.hubpay.model;

public enum TransactionEnum {

    DEBIT("Debited"), CREDIT("Credited");

    private final String status;

    TransactionEnum(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
