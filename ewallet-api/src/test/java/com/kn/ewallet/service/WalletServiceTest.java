package com.kn.ewallet.service;

import com.kn.ewallet.dto.WalletDto;
import com.kn.ewallet.entity.Wallet;
import com.kn.ewallet.exception.HttpException;
import com.kn.ewallet.repository.WalletRepository;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.kn.ewallet.util.HttpExceptionMatcher.hasHttpStatus;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class WalletServiceTest {


    @Mock
    private WalletRepository walletRepository;

    private WalletService walletService;

    @Captor
    ArgumentCaptor<Wallet> walletArgumentCaptor;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() {
        walletService = new WalletService(walletRepository);
    }

    @Test
    public void insert() {
        String walletName = "mock_name";
        WalletDto mockDto = new WalletDto();
        mockDto.setName(walletName);

        Wallet output = new Wallet();
        output.setName(walletName);
        output.setId(1);

        when(walletRepository.save(walletArgumentCaptor.capture())).thenReturn(output);

        WalletDto saved = walletService.insert(mockDto);
        verify(walletRepository).save(walletArgumentCaptor.capture());
        assertEquals(walletName, walletArgumentCaptor.getValue().getName());

        assertEquals(walletName, saved.getName());
        assertEquals(1, saved.getId().intValue());

    }

    @Test
    public void update() {
        String walletName = "mock_name";
        WalletDto mockDto = new WalletDto();
        mockDto.setName(walletName);

        Wallet output = new Wallet();
        output.setName(walletName);
        output.setId(1);

        when(walletRepository.findById(1)).thenReturn(Optional.of(new Wallet()));
        when(walletRepository.save(walletArgumentCaptor.capture())).thenReturn(output);

        WalletDto saved = walletService.update(1, mockDto);
        verify(walletRepository).save(walletArgumentCaptor.capture());
        assertEquals(walletName, walletArgumentCaptor.getValue().getName());

        assertEquals(walletName, saved.getName());
        assertEquals(1, saved.getId().intValue());
    }

    @Test
    public void update_when_wallet_not_found_it_should_throw_404() {
        WalletDto mockDto = new WalletDto();
        mockDto.setName("mock_name");

        when(walletRepository.findById(1)).thenReturn(Optional.empty());

        expectedException.expect(HttpException.class);
        expectedException.expectMessage("No wallet found for id 1");
        expectedException.expect(hasHttpStatus(HttpStatus.NOT_FOUND));
        walletService.update(1, mockDto);
    }

    @Test
    public void viewWallet() {
        Wallet mockWallet = new Wallet();
        mockWallet.setName("mock_name");
        mockWallet.setId(1);

        when(walletRepository.findById(1)).thenReturn(Optional.of(mockWallet));
        WalletDto result = walletService.viewWallet(1);
        assertEquals("mock_name", result.getName());
        assertEquals(1, result.getId().intValue());
    }

    @Test
    public void viewWallet_when_wallet_not_found_it_should_throw_404() {
        when(walletRepository.findById(1)).thenReturn(Optional.empty());

        expectedException.expect(HttpException.class);
        expectedException.expectMessage("No wallet found for id 1");
        expectedException.expect(hasHttpStatus(HttpStatus.NOT_FOUND));

        walletService.viewWallet(1);
    }

    @Test
    public void viewAllWallet() {

        Wallet mockWallet1 = new Wallet();
        mockWallet1.setName("mock_name_1");
        mockWallet1.setId(1);

        Wallet mockWallet2 = new Wallet();
        mockWallet2.setName("mock_name_2");
        mockWallet2.setId(2);

        when(walletRepository.findAll()).thenReturn(Arrays.asList(mockWallet1, mockWallet2));
        List<WalletDto> result = walletService.viewAllWallet();

        assertEquals(2, result.size());
        assertThat(result,  hasItem(allOf(
                Matchers.<WalletDto>hasProperty("id", is(1)),
                Matchers.<WalletDto>hasProperty("name", is("mock_name_1"))
        )));
        assertThat(result,  hasItem(allOf(
                Matchers.<WalletDto>hasProperty("id", is(2)),
                Matchers.<WalletDto>hasProperty("name", is("mock_name_2"))
        )));
    }
}