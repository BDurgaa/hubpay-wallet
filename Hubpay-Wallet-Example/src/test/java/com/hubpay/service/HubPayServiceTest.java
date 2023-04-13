package com.hubpay.service;

import com.hubpay.exception.UserNotFoundException;
import com.hubpay.exception.WalletBadRequest;
import com.hubpay.exception.WalletNotFoundException;
import com.hubpay.model.Transaction;
import com.hubpay.model.User;
import com.hubpay.model.Wallet;
import com.hubpay.model.WalletDetailsDTO;
import com.hubpay.repository.TransactionRepository;
import com.hubpay.repository.UserRepository;
import com.hubpay.repository.WalletRepository;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HubPayServiceTest {

    @InjectMocks
    private HubPayService hubPayService;

    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private WalletRepository walletRepository;

    @Before
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addFundsToWalletWhenUserNotFoundTest() {
        WalletDetailsDTO walletDetailsDTO = new WalletDetailsDTO();
        walletDetailsDTO.setUserId("hp1234");
        walletDetailsDTO.setAmount(100d);
        assertThrows(UserNotFoundException.class, () -> hubPayService.addFundsToWallet(walletDetailsDTO));
    }

    @Test
    void addFundsToWalletTest() {
        WalletDetailsDTO walletDetailsDTO = new WalletDetailsDTO();
        walletDetailsDTO.setUserId("HP1234");
        walletDetailsDTO.setAmount(50d);
        Optional<User> optionalUser = getUser();
        when(userRepository.findById(anyString())).thenReturn(optionalUser);
        when(walletRepository.save(any(Wallet.class))).thenReturn(mock(Wallet.class));
        hubPayService.addFundsToWallet(walletDetailsDTO);
    }

    @Test
    void getWalletBalanceTest() {
        String user = "HP1234";
        when(walletRepository.findWalletById(user)).thenReturn(mock(Wallet.class));
        hubPayService.getWalletBalance("HP1234");
    }

    @Test
    void getWalletBalanceWithoutWalletTest() {
        String user = "HP1234";
        when(walletRepository.findWalletById(user)).thenReturn(null);
        assertThrows(WalletNotFoundException.class, () -> hubPayService.getWalletBalance("HP1234"));
    }

    @Test
    void withdrawWalletBalanceTest() throws Exception {
        WalletDetailsDTO walletDetailsDTO = new WalletDetailsDTO();
        walletDetailsDTO.setUserId("HP1234");
        walletDetailsDTO.setAmount(50d);
        when(userRepository.findById(anyString())).thenReturn(getUser());
        Wallet wallet = new Wallet();
        wallet.setBalance(100d);
        wallet.setId("HP1234");
        when(walletRepository.findWalletById(anyString())).thenReturn(wallet);
        hubPayService.withdrawWalletBalance(walletDetailsDTO);
    }

    @Test
    void withdrawFromWalletWithOutBalanceTest() {
        WalletDetailsDTO walletDetailsDTO = new WalletDetailsDTO();
        walletDetailsDTO.setUserId("HP1234");
        walletDetailsDTO.setAmount(50d);
        when(userRepository.findById(anyString())).thenReturn(getUser());
        Wallet wallet = new Wallet();
        wallet.setId("HP1234");
        wallet.setBalance(1d);
        when(walletRepository.findWalletById(anyString())).thenReturn(wallet);
        assertThrows(WalletBadRequest.class, () ->
                hubPayService.withdrawWalletBalance(walletDetailsDTO));
    }

    @Test
    void getStatementTest() {
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction());
        transactions.get(0).setUserId("HP1234");
        transactions.get(0).setStatus("Credited");
        transactions.get(0).setAmount(50d);
        transactions.get(0).setDate(new Date());
        when(transactionRepository.findByUserId(anyString())).thenReturn(transactions);
        hubPayService.getStatement("HP1234", null, null);
    }

    private static Optional<User> getUser() {
        User user = new User();
        user.setWallet(new Wallet());
        user.setId("HP1234");
        user.setUserName("Hubpay");
        user.setUserEmail("user@hubpay.ae");
        Optional<User> optionalUser = Optional.of(user);
        return optionalUser;
    }
}