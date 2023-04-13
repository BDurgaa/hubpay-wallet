package com.hubpay.service;

import com.hubpay.exception.TransactionBadRequest;
import com.hubpay.exception.UserNotFoundException;
import com.hubpay.exception.WalletBadRequest;
import com.hubpay.exception.WalletNotFoundException;
import com.hubpay.model.*;
import com.hubpay.repository.TransactionRepository;
import com.hubpay.repository.UserRepository;
import com.hubpay.repository.WalletRepository;
import com.hubpay.validation.ValidationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class HubPayService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final WalletRepository walletRepository;

    public String addFundsToWallet(WalletDetailsDTO request) {
        log.info("inside UserService.addFundsToWallet(.)");
        ValidationUtil.validateWalletPayments(request.getAmount());

        //validate user
        Optional<User> hubpayUser = userRepository.findById(request.getUserId());
        if (hubpayUser.isEmpty())
            throw new UserNotFoundException("User not found for this request");

        //update wallet details
        Wallet wallet = hubpayUser.get().getWallet();// walletRepository.findWalletById(request.getUid());
        wallet.setVersion(new Date());
        if (wallet.getBalance() != null)
            wallet.setBalance(request.getAmount() + wallet.getBalance());
        else
            wallet.setBalance(request.getAmount());

        //update transaction details
        Transaction transaction = new Transaction();
        transaction.setAmount(request.getAmount());
        transaction.setStatus(TransactionEnum.CREDIT.getStatus());
        transaction.setUserId(wallet.getId());
        transaction.setDate(new Date(Calendar.getInstance().getTime().getTime()));
        wallet.getTransactions().add(transaction);

        walletRepository.save(wallet);
        return "You have successfully added funds to your wallet, total balance is £ " + String.format("%.2f", wallet.getBalance());
    }

    @Transactional
    public String getWalletBalance(String userId) {
        log.info("inside getWalletBalance().");
        Wallet wallet = walletRepository.findWalletById(userId);
        if (wallet == null)
            throw new WalletNotFoundException("Wallet Not Found for " + userId);
        return "You current wallet balance is £ " + String.format("%.2f", wallet.getBalance());
    }

    public synchronized String withdrawWalletBalance(WalletDetailsDTO withdrawRequest) throws Exception {
        log.info("current thread name : " + Thread.currentThread().getName());
        ValidationUtil.validateWithdrawAmount(withdrawRequest.getAmount());

        User user = this.getHubPayUser(withdrawRequest.getUserId());

        Wallet userWallet = walletRepository.findWalletById(String.valueOf(user.getId()));

        if (userWallet.getBalance() < withdrawRequest.getAmount()) {
            throw new WalletBadRequest("You don't have Sufficient Balance");
        }
        userWallet.setVersion(new Date());
        userWallet.setBalance(userWallet.getBalance() - withdrawRequest.getAmount());
        Transaction transaction = new Transaction();
        transaction.setAmount(withdrawRequest.getAmount());
        transaction.setDate(new Date(Calendar.getInstance().getTime().getTime()));
        transaction.setStatus(TransactionEnum.DEBIT.getStatus());
        userWallet.getTransactions().add(transaction);
        log.info(String.format("$$ -> Producing Transaction --> %s", transaction));
        walletRepository.saveAndFlush(userWallet);
        log.info("successfully completed withdrawn and updated the transaction");
        return "You have successfully withdrawn £ " + String.format("%.2f", withdrawRequest.getAmount()) + ", and remaining balance is : £ " + String.format("%.2f", userWallet.getBalance());
    }

    private User getHubPayUser(String userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty())
            throw new UserNotFoundException("User not found for " + userId);

        return optionalUser.get();
    }

    public String getStatement(String userId, Integer offset, Integer pageSize) {
        log.info("inside getStatement for user {} with offset {} and page size {} ", userId, offset, pageSize);
        return sendTxnHistory(userId, offset, pageSize);
    }

    private String sendTxnHistory(String id, Integer offset, Integer pageSize) {

        Page<Transaction> transactionsPage;
        List<Transaction> list;

        if (offset != null && pageSize != null) {
            transactionsPage = transactionRepository.findByUserId(id, PageRequest.of(offset, pageSize));
            list = transactionsPage.stream().toList();
        } else {
            list = transactionRepository.findByUserId(id);
        }

        if (list.isEmpty())
            throw new TransactionBadRequest("No transactions found for user " + id);

        StringBuffer fw = new StringBuffer();
        fw.append("---------------Transaction History---------------");
        fw.append("\n");
        fw.append("Tran Id| Amount  | Status  |  User  |  Date  ");
        try {
            for (int i = 0; i < list.size(); i++) {
                fw.append("\n");
                fw.append(list.get(i).getId());
                fw.append("      | ");
                fw.append("£ " + String.format("%.2f", list.get(i).getAmount()));
                fw.append(" | ");
                fw.append(list.get(i).getStatus());
                fw.append(" | ");
                fw.append(list.get(i).getUserId());
                fw.append(" | ");
                fw.append(list.get(i).getDate().toString());
                fw.append("\n");
                fw.append("------------------------------------------------------");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fw.toString();
    }
}