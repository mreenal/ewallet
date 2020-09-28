package com.kn.ewallet.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kn.ewallet.dto.WalletDto;
import com.kn.ewallet.exception.HttpException;
import com.kn.ewallet.service.FundService;
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

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class FundControllerTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    private FundController fundController;

    @Mock
    private FundService fundService;


    @Before
    public void setup() {
        initMocks(this);
        fundController = new FundController(fundService);
        mockMvc = MockMvcBuilders.standaloneSetup(fundController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void addFund() throws Exception {
        String walletName = "mock_wallet";
        WalletDto payLoad = new WalletDto();
        payLoad.setFundAmount(2.1);

        WalletDto response = new WalletDto();
        response.setName(walletName);
        response.setId(1);
        response.setFundAmount(3.1);

        ArgumentCaptor<Integer> walletIdArgumentCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<WalletDto> payLoadArgumentCaptor = ArgumentCaptor.forClass(WalletDto.class);
        when(fundService.addFund(walletIdArgumentCaptor.capture(), payLoadArgumentCaptor.capture())).thenReturn(response);

        mockMvc.perform(post("/api/v1/wallet/{id}/topup", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                        payLoad
                )))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value(walletName))
                .andExpect(jsonPath("$.fund_amount").value(3.1))
        ;

        verify(fundService).addFund(walletIdArgumentCaptor.capture(), payLoadArgumentCaptor.capture());

        assertEquals(1, walletIdArgumentCaptor.getValue().intValue());
        assertEquals(2.1, payLoadArgumentCaptor.getValue().getFundAmount(), 0);
    }

    @Test
    public void withdrawFund() throws Exception {
        String walletName = "mock_wallet";
        WalletDto payLoad = new WalletDto();
        payLoad.setFundAmount(1.1);

        WalletDto response = new WalletDto();
        response.setName(walletName);
        response.setId(1);
        response.setFundAmount(2.1);

        ArgumentCaptor<Integer> walletIdArgumentCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<WalletDto> payLoadArgumentCaptor = ArgumentCaptor.forClass(WalletDto.class);
        when(fundService.withdrawFund(walletIdArgumentCaptor.capture(), payLoadArgumentCaptor.capture())).thenReturn(response);

        mockMvc.perform(post("/api/v1/wallet/{id}/withdraw", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                        payLoad
                )))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value(walletName))
                .andExpect(jsonPath("$.fund_amount").value(2.1))
        ;

        verify(fundService).withdrawFund(walletIdArgumentCaptor.capture(), payLoadArgumentCaptor.capture());

        assertEquals(1, walletIdArgumentCaptor.getValue().intValue());
        assertEquals(1.1, payLoadArgumentCaptor.getValue().getFundAmount(), 0);
    }

    @Test
    public void withdrawFund_error() throws Exception {
        String walletName = "mock_wallet";
        WalletDto payLoad = new WalletDto();
        payLoad.setFundAmount(1.1);

        ArgumentCaptor<Integer> walletIdArgumentCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<WalletDto> payLoadArgumentCaptor = ArgumentCaptor.forClass(WalletDto.class);
        when(fundService.withdrawFund(walletIdArgumentCaptor.capture(), payLoadArgumentCaptor.capture()))
                .thenThrow(new HttpException("Funds on wallet 1 can not go below zero.", HttpStatus.UNPROCESSABLE_ENTITY));

        mockMvc.perform(post("/api/v1/wallet/{id}/withdraw", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                        payLoad
                )))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print())
                .andExpect(jsonPath("$.message").value("Funds on wallet 1 can not go below zero."))
        ;

        verify(fundService).withdrawFund(walletIdArgumentCaptor.capture(), payLoadArgumentCaptor.capture());

        assertEquals(1, walletIdArgumentCaptor.getValue().intValue());
        assertEquals(1.1, payLoadArgumentCaptor.getValue().getFundAmount(), 0);
    }
}