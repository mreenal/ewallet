package com.kn.ewallet.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kn.ewallet.entity.Wallet;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WalletDto {

    public WalletDto() {
    }

    public WalletDto(Wallet entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.fundAmount = entity.getFundAmount();
    }

    private Integer id;

    private String name;

    @JsonProperty("fund_amount")
    private double fundAmount;


}
