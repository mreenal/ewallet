package com.kn.ewallet.service;

import com.kn.ewallet.dto.WalletDto;
import com.kn.ewallet.entity.Wallet;
import com.kn.ewallet.exception.HttpException;
import com.kn.ewallet.repository.WalletRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class WalletService {

    private final WalletRepository walletRepository;

    @Autowired
    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    public WalletDto insert(WalletDto dto) {
        log.info("inserting new wallet");
        Wallet entity = new Wallet();
        entity.setName(dto.getName());
        return save(entity);
    }

    public WalletDto update(Integer id, WalletDto dto) {
        log.info("updating wallet info {}", id);
        Wallet existingEntity = getWallet(id);
        existingEntity.setName(dto.getName());
        return save(existingEntity);
    }

    public WalletDto viewWallet(Integer id) {
        log.info("getting wallet info for {}", id);
        Wallet wallet = getWallet(id);
        return new WalletDto(wallet);
    }

    public List<WalletDto> viewAllWallet() {
        log.info("getting wallet info for all");
        List<Wallet> wallets = walletRepository.findAll();
        return wallets.stream().map(WalletDto::new).collect(Collectors.toList());
    }

    private Wallet getWallet(Integer id) {
        log.info("finding wallet info for {}", id);
        Optional<Wallet> existingEntity = walletRepository.findById(id);
        if (!existingEntity.isPresent()) {
            log.error("No wallet found for id {}", id);
            throw new HttpException(String.format("No wallet found for id %d", id), HttpStatus.NOT_FOUND);
        }
        return existingEntity.get();
    }

    private WalletDto save(Wallet entity) {
        Wallet saved = walletRepository.save(entity);
        return new WalletDto(saved);
    }

}
