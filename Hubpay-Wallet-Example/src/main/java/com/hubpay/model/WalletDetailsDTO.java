package com.hubpay.model;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WalletDetailsDTO {

    @NotNull(message = "User Id is mandatory")
    private String userId;
    @NotNull(message = "Amount is mandatory")
    private Double amount;
}
