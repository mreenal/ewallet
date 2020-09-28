package com.kn.ewallet.service;

import com.kn.ewallet.dto.WalletDto;
import com.kn.ewallet.entity.Wallet;
import com.kn.ewallet.exception.HttpException;
import com.kn.ewallet.repository.WalletRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class FundService {

    private final WalletRepository walletRepository;

    public FundService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    public WalletDto addFund(Integer id, WalletDto input) {
        Wallet wallet = getWallet(id);
        double newFund = wallet.getFundAmount() + input.getFundAmount();
        wallet.setFundAmount(newFund);
        Wallet updated = walletRepository.save(wallet);
        return new WalletDto(updated);
    }

    public WalletDto withdrawFund(Integer id, WalletDto input) {
        Wallet wallet = getWallet(id);
        double newFund = wallet.getFundAmount() - input.getFundAmount();
        if (newFund < 0) {
            log.error("Funds on wallet {} can not go below zero.", id);
            throw new HttpException(String.format("Funds on wallet %d can not go below zero.", id), HttpStatus.UNPROCESSABLE_ENTITY);
        }
        wallet.setFundAmount(newFund);
        Wallet updated = walletRepository.save(wallet);
        return new WalletDto(updated);
    }

    private Wallet getWallet(Integer id) {
        Optional<Wallet> wallet = walletRepository.findById(id);
        if (!wallet.isPresent()) {
            log.error("No wallet found for id {}", id);
            throw new HttpException(String.format("No wallet found for id %d", id), HttpStatus.NOT_FOUND);
        }
        return wallet.get();
    }
}
