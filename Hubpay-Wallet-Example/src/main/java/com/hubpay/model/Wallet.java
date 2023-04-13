package com.hubpay.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "HUBPAY_WALLET")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Wallet {

    @Id
    @Column(name = "user_id")
    private String id;

    @Column(name = "balance")
    private Double balance;

    @Column(name = "is_active")
    private Boolean isActive;

    @Version
    private Date version;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "user_id")
    private List<Transaction> transactions = new ArrayList<>();

}