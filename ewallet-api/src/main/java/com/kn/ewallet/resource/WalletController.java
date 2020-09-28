package com.kn.ewallet.resource;

import com.kn.ewallet.dto.WalletDto;
import com.kn.ewallet.service.WalletService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api")
@CrossOrigin
public class WalletController extends BaseController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping(value = "/v1/wallet")
    public ResponseEntity<WalletDto> insert(@RequestBody WalletDto input) {
        WalletDto output = walletService.insert(input);
        return ResponseEntity.ok(output);
    }

    @PutMapping(value = "/v1/wallet/{id}")
    public ResponseEntity<WalletDto> update(@RequestBody WalletDto input, @PathVariable("id") Integer id) {
        WalletDto output = walletService.update(id, input);
        return ResponseEntity.ok(output);
    }

    @GetMapping(value = "/v1/wallet/{id}")
    public ResponseEntity<WalletDto> viewWallet(@PathVariable("id") Integer id) {
        WalletDto output = walletService.viewWallet(id);
        return ResponseEntity.ok(output);
    }

    @GetMapping(value = "/v1/wallet")
    public ResponseEntity<List<WalletDto>> viewAllWallet() {
        List<WalletDto> output = walletService.viewAllWallet();
        return ResponseEntity.ok(output);
    }


}
