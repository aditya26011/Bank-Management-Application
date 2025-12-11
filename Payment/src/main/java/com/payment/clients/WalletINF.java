package com.payment.clients;

import com.payment.entities.CreditDebitReq;
import com.payment.entities.TransferRequestDTO;
import com.payment.entities.Wallet;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(url= "http://localhost:8082", value = "Wallet-client")
public interface WalletINF {

    @GetMapping("/wallet/getBalance")
    public Long getBalance(@RequestHeader("X-User-Id") Long userId);

    @PostMapping(value = "/wallet/credit")
    public Boolean credit(@RequestBody CreditDebitReq creditDebitReq);

    @PostMapping("/wallet/debit")
    public Boolean debit(@RequestBody CreditDebitReq creditDebitReq);

    @GetMapping("/wallet/{id}")
    public Wallet getWallet(@PathVariable Long id);

    @PostMapping("/wallet/transfer")
    public Boolean transfer(@RequestBody TransferRequestDTO transferRequestDTO);


}
