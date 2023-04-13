package com.hubpay.repository;

import com.hubpay.model.Wallet;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, String> {

    //@Lock(LockModeType.PESSIMISTIC_READ)
    Wallet findWalletById(String userId);
}
