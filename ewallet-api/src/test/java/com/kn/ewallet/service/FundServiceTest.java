package com.kn.ewallet.service;

import com.kn.ewallet.dto.WalletDto;
import com.kn.ewallet.entity.Wallet;
import com.kn.ewallet.exception.HttpException;
import com.kn.ewallet.repository.WalletRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static com.kn.ewallet.util.HttpExceptionMatcher.hasHttpStatus;
import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FundServiceTest {

    private FundService fundService;

    @Mock
    private WalletRepository walletRepository;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();


    @Before
    public void setUp() throws Exception {
        fundService = new FundService(walletRepository);
    }

    @Test
    public void addFund() {

        WalletDto input = new WalletDto();
        input.setFundAmount(50);

        Wallet existing = new Wallet();
        existing.setFundAmount(100);

        when(walletRepository.findById(1)).thenReturn(Optional.of(existing));
        ArgumentCaptor<Wallet> entityArgumentCaptor = ArgumentCaptor.forClass(Wallet.class);
        when(walletRepository.save(entityArgumentCaptor.capture())).thenReturn(new Wallet());

        fundService.addFund(1, input);

        verify(walletRepository).save(entityArgumentCaptor.capture());

        assertEquals(150, entityArgumentCaptor.getValue().getFundAmount(), 0);
    }

    @Test
    public void when_add_to_an_invalid_fund_id() {

        WalletDto input = new WalletDto();

        when(walletRepository.findById(1)).thenReturn(Optional.empty());

        expectedException.expect(HttpException.class);
        expectedException.expectMessage("No wallet found for id 1");
        expectedException.expect(hasHttpStatus(HttpStatus.NOT_FOUND));

        fundService.addFund(1, input);
    }

    @Test
    public void withdrawFund() {
        WalletDto input = new WalletDto();
        input.setFundAmount(40);

        Wallet existing = new Wallet();
        existing.setFundAmount(100);

        when(walletRepository.findById(1)).thenReturn(Optional.of(existing));
        ArgumentCaptor<Wallet> entityArgumentCaptor = ArgumentCaptor.forClass(Wallet.class);
        when(walletRepository.save(entityArgumentCaptor.capture())).thenReturn(new Wallet());

        fundService.withdrawFund(1, input);

        verify(walletRepository).save(entityArgumentCaptor.capture());

        assertEquals(60, entityArgumentCaptor.getValue().getFundAmount(), 0);
    }

    @Test
    public void when_withdraw_more_than_balance_it_should_throw_error() {
        WalletDto input = new WalletDto();
        input.setFundAmount(200);

        Wallet existing = new Wallet();
        existing.setFundAmount(100);

        when(walletRepository.findById(1)).thenReturn(Optional.of(existing));

        expectedException.expect(HttpException.class);
        expectedException.expectMessage("Funds on wallet 1 can not go below zero.");
        expectedException.expect(hasHttpStatus(HttpStatus.UNPROCESSABLE_ENTITY));

        fundService.withdrawFund(1, input);
    }

    @Test
    public void when_withdraw_from_an_invalid_fund_id() {

        WalletDto input = new WalletDto();

        when(walletRepository.findById(1)).thenReturn(Optional.empty());

        expectedException.expect(HttpException.class);
        expectedException.expectMessage("No wallet found for id 1");
        expectedException.expect(hasHttpStatus(HttpStatus.NOT_FOUND));

        fundService.withdrawFund(1, input);
    }

}