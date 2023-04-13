package com.hubpay.exception;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ErrorResponse {

    public ErrorResponse(String message, ArrayList<String> details) {
        super();
        this.message = message;
        this.details = details;
    }

    private String message;
    private ArrayList<String> details;
}