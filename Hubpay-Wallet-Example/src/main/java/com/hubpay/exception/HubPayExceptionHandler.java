package com.hubpay.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;

/*
    Centralized Exception handler class to handle all application related, business logic related exceptions
 */
@ControllerAdvice
public class HubPayExceptionHandler extends ResponseEntityExceptionHandler {

    /*
        This handles when user not found in the database or invalid user
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex) {
        var details = new ArrayList<String>();
        details.add(ex.getLocalizedMessage());
        ErrorResponse error = new ErrorResponse("INVALID_USER", details);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    /*
        This handles when wallet not found for requested user
     */
    @ExceptionHandler(WalletNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleWalletNotFound(WalletNotFoundException ex) {
        var details = new ArrayList<String>();
        details.add(ex.getLocalizedMessage());
        ErrorResponse error = new ErrorResponse("WALLET_NOT_FOUND", details);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    /*
        This handles when there are no enough funds in the wallet during withdrawl
     */
    @ExceptionHandler(WalletBadRequest.class)
    public ResponseEntity<ErrorResponse> handleWalletBadRequest(WalletBadRequest ex) {
        var details = new ArrayList<String>();
        details.add(ex.getLocalizedMessage());
        ErrorResponse error = new ErrorResponse("INSUFFICIENT_FUNDS", details);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /*
        This handles for minimum and maximum deposits and maximum withdrawl requests
     */
    @ExceptionHandler(TransactionBadRequest.class)
    public ResponseEntity<ErrorResponse> handleTransactionBadRequest(TransactionBadRequest ex) {
        var details = new ArrayList<String>();
        details.add(ex.getLocalizedMessage());
        ErrorResponse error = new ErrorResponse("INVALID_TRANSACTION", details);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /*
        This handles for any other run time exception during processing
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
        var details = new ArrayList<String>();
        details.add(ex.getLocalizedMessage());
        ErrorResponse error = new ErrorResponse("SERVER_ERROR", details);
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}