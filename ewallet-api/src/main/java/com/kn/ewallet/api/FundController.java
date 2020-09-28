package com.kn.ewallet.api;

import com.kn.ewallet.dto.WalletDto;
import com.kn.ewallet.service.FundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@CrossOrigin
@RequestMapping("/api")
public class FundController extends BaseController {

    private final FundService fundService;

    @Autowired
    public FundController(FundService fundService) {
        this.fundService = fundService;
    }


    @PostMapping(value = "/v1/wallet/{id}/topup")
    public ResponseEntity<WalletDto> addFund(@RequestBody WalletDto input, @PathVariable("id") Integer id) {
        WalletDto output = fundService.addFund(id, input);
        return ResponseEntity.ok(output);
    }

    @PostMapping(value = "/v1/wallet/{id}/withdraw")
    public ResponseEntity<WalletDto> withdrawFund(@RequestBody WalletDto input, @PathVariable("id") Integer id) {
        WalletDto output = fundService.withdrawFund(id, input);
        return ResponseEntity.ok(output);
    }

}
