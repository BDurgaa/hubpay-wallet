package com.hubpay.validation;

import com.hubpay.exception.TransactionBadRequest;

public class ValidationUtil {

    public static void validateWalletPayments(Double amount) {

        if (amount < 10)
            throw new TransactionBadRequest("Minimum amount should be £ 10");
        if (amount > 10000)
            throw new TransactionBadRequest("Maximum amount should be £ 10,000");
    }

    public static void validateWithdrawAmount(Double amount) {

        if (amount > 5000)
            throw new TransactionBadRequest("Maximum amount can be withdrawn is £ 5000 only, Kindly enter less than that");
    }
}
