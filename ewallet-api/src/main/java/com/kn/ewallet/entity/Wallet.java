package com.kn.ewallet.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "wallet")
@Getter
@Setter
public class Wallet {

    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;

    @Column(name = "wallet_name")
    private String name;

    @Column(name = "fund_amount")
    private double fundAmount;

}
