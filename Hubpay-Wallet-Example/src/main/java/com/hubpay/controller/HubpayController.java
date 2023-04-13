package com.hubpay.controller;

import com.hubpay.model.WalletDetailsDTO;
import com.hubpay.service.HubPayService;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Log4j2
public class HubpayController {

    private final HubPayService hubPayService;
    private String withdrawnRespMsg;

    /*
        This method adds funds to wallet, userId and amount need to be passed
        as part of the request
     */
    @PostMapping("/addFunds")
    public String addFunds(@RequestBody @Valid WalletDetailsDTO request) {
        log.info("inside HubPay addBalance(.) {} ", request);
        return hubPayService.addFundsToWallet(request);
    }

    /*
        This method return wallet balance for given user
     */
    @GetMapping("/getBal/{id}")
    public String getBalance(@PathVariable String id) throws Exception {
        log.info("inside HubPay getBalance(.) for user {} ", id);
        return hubPayService.getWalletBalance(id);
    }

    /*
        after successful withdrawal of funds from wallet, requested page would be redirected to new page to avoid
        due form submission
     */
    @PutMapping("/withdrawMoney")
    void withdrawMoney(@RequestBody WalletDetailsDTO walletDetailsDTO, HttpServletResponse response) throws Exception {
        log.info("inside HubPay withdrawMoney(.) for user {} ", walletDetailsDTO.getUserId());
        withdrawnRespMsg = hubPayService.withdrawWalletBalance(walletDetailsDTO);
        response.sendRedirect("/success");
    }

    /*
        This method displays the transaction history for the given user
     */
    @GetMapping("/txnHistory/{id}")
    public String getTransactionHistory(@PathVariable String id, @RequestParam("offset") @Nullable Integer offset,
                                        @RequestParam("pageSize") @Nullable Integer pageSize) {
        log.info(String.format("$$ -> Producing Transaction --> %s", id));
        return hubPayService.getStatement(id, offset, pageSize);
    }

    @GetMapping("/success")
    @ResponseBody
    public String transactionDone() {
        return withdrawnRespMsg;
    }

}
