package com.kn.ewallet.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kn.ewallet.dto.WalletDto;
import com.kn.ewallet.exception.HttpException;
import com.kn.ewallet.service.WalletService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class WalletControllerTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    private WalletController walletController;

    @Mock
    private WalletService walletService;


    @Before
    public void setup() {
        initMocks(this);
        walletController = new WalletController(walletService);
        mockMvc = MockMvcBuilders.standaloneSetup(walletController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void insert() throws Exception {

        String walletName = "mock_wallet";
        WalletDto payLoad = new WalletDto();
        payLoad.setName(walletName);

        WalletDto response = new WalletDto();
        response.setName(walletName);
        response.setId(1);

        ArgumentCaptor<WalletDto> payLoadArgumentCaptor = ArgumentCaptor.forClass(WalletDto.class);
        when(walletService.insert(payLoadArgumentCaptor.capture())).thenReturn(response);

        mockMvc.perform(post("/api/v1/wallet")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                        payLoad
                )))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value(walletName))
        ;

        verify(walletService).insert(payLoadArgumentCaptor.capture());

        assertEquals(walletName, payLoadArgumentCaptor.getValue().getName());

    }

    @Test
    public void update() throws Exception {
        String walletName = "mock_wallet";
        int walletId = 1;
        WalletDto payLoad = new WalletDto();
        payLoad.setName(walletName);

        WalletDto response = new WalletDto();
        response.setName(walletName);
        response.setId(walletId);

        ArgumentCaptor<Integer> walletIdArgumentCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<WalletDto> payLoadArgumentCaptor = ArgumentCaptor.forClass(WalletDto.class);
        when(walletService.update(walletIdArgumentCaptor.capture(), payLoadArgumentCaptor.capture())).thenReturn(response);

        mockMvc.perform(put("/api/v1/wallet/{id}", walletId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                        payLoad
                )))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").value(walletId))
                .andExpect(jsonPath("$.name").value(walletName))
        ;

        verify(walletService).update(walletIdArgumentCaptor.capture(), payLoadArgumentCaptor.capture());

        assertEquals(1, walletIdArgumentCaptor.getValue().intValue());
        assertEquals(walletName, payLoadArgumentCaptor.getValue().getName());
    }

    @Test
    public void viewWallet() throws Exception {
        String walletName = "mock_wallet";
        int walletId = 1;

        WalletDto response = new WalletDto();
        response.setName(walletName);
        response.setId(walletId);
        response.setFundAmount(1.1);

        ArgumentCaptor<Integer> walletIdArgumentCaptor = ArgumentCaptor.forClass(Integer.class);
        when(walletService.viewWallet(walletIdArgumentCaptor.capture())).thenReturn(response);

        mockMvc.perform(get("/api/v1/wallet/{id}", walletId))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").value(walletId))
                .andExpect(jsonPath("$.name").value(walletName))
                .andExpect(jsonPath("$.fund_amount").value(1.1))
        ;

        verify(walletService).viewWallet(walletIdArgumentCaptor.capture());

        assertEquals(1, walletIdArgumentCaptor.getValue().intValue());
    }

    @Test
    public void viewAllWallet() throws Exception {

        WalletDto response1 = new WalletDto();
        response1.setName("wallet_1");
        response1.setId(1);
        response1.setFundAmount(1.1);
        WalletDto response2 = new WalletDto();
        response2.setName("wallet_2");
        response2.setId(2);
        response2.setFundAmount(2.2);
        List<WalletDto> response = Arrays.asList(response1, response2);

        when(walletService.viewAllWallet()).thenReturn(response);

        mockMvc.perform(get("/api/v1/wallet"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$").value(hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("wallet_1"))
                .andExpect(jsonPath("$[0].fund_amount").value(1.1))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("wallet_2"))
                .andExpect(jsonPath("$[1].fund_amount").value(2.2))
        ;
    }

    @Test
    public void update_exception_not_found() throws Exception {
        String walletName = "mock_wallet";
        int walletId = 1;
        WalletDto payLoad = new WalletDto();
        payLoad.setName(walletName);

        ArgumentCaptor<Integer> walletIdArgumentCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<WalletDto> payLoadArgumentCaptor = ArgumentCaptor.forClass(WalletDto.class);
        when(walletService.update(walletIdArgumentCaptor.capture(), payLoadArgumentCaptor.capture()))
                .thenThrow(new HttpException("not found", HttpStatus.NOT_FOUND));

        mockMvc.perform(put("/api/v1/wallet/{id}", walletId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                        payLoad
                )))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andExpect(jsonPath("$.message").value("not found"))
        ;

        verify(walletService).update(walletIdArgumentCaptor.capture(), payLoadArgumentCaptor.capture());

        assertEquals(1, walletIdArgumentCaptor.getValue().intValue());
        assertEquals(walletName, payLoadArgumentCaptor.getValue().getName());
    }
}