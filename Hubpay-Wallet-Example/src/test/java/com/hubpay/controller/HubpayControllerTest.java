package com.hubpay.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hubpay.exception.TransactionBadRequest;
import com.hubpay.exception.WalletNotFoundException;
import com.hubpay.model.WalletDetailsDTO;
import com.hubpay.model.Transaction;
import com.hubpay.service.HubPayService;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = HubpayController.class)
class HubpayControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HubPayService hubPayService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addFunds() throws Exception {

        var addBalanceDetails = new WalletDetailsDTO();
        addBalanceDetails.setAmount(100d);
        addBalanceDetails.setUserId("HP1234");

        var objectMapper = new ObjectMapper();
        var data = objectMapper.writeValueAsString(addBalanceDetails);

        when(hubPayService.addFundsToWallet(any(WalletDetailsDTO.class))).thenReturn("You have successfully added funds");

        MvcResult mvcResult = mockMvc.perform(
                        post("/addFunds").
                                content(data).
                                contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        Assert.assertEquals("You have successfully added funds", mvcResult.getResponse().getContentAsString());
        Assert.assertTrue(mvcResult.getResponse().getStatus() == 200);
    }

    @Test
    void getBalance() throws Exception {
        when(hubPayService.getWalletBalance("HP1234")).thenReturn("£ 100");
        MvcResult mvcResult = mockMvc.perform(
                        get("/getBal/{id}", "HP1234").
                                contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();
        Assert.assertTrue(mvcResult.getResponse().getStatus() == 200);
        Assert.assertEquals("£ 100", mvcResult.getResponse().getContentAsString());
    }


    @Test
    void withdrawMoney() throws Exception {
        Transaction transaction = new Transaction();
        transaction.setUserId("HP1234");
        transaction.setAmount(30d);
        when(hubPayService.withdrawWalletBalance(any(WalletDetailsDTO.class))).thenReturn("you have withdrawn successfully");
        var objectMapper = new ObjectMapper();
        var data = objectMapper.writeValueAsString(transaction);
        MvcResult mvcResult = mockMvc.perform(
                        put("/withdrawMoney").content(data).
                                contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is3xxRedirection())
                .andReturn();

        Assert.assertTrue(mvcResult.getResponse().getStatus() == 302);
        Assert.assertEquals("", mvcResult.getResponse().getContentAsString());
    }

    @Test
    void getTransactionHistory() throws Exception {
        when(hubPayService.getStatement("HP1234",null,null)).thenReturn("statement retrieved");
        MvcResult mvcResult = mockMvc.perform(
                        get("/txnHistory/{id}", "HP1234").
                                contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();
        Assert.assertTrue(mvcResult.getResponse().getStatus() == 200);
        Assert.assertEquals("statement retrieved", mvcResult.getResponse().getContentAsString());
    }
}