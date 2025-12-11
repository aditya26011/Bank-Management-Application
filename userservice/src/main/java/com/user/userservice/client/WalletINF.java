package com.user.userservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(url= "http://localhost:8082", value = "wallet-client")

public interface WalletINF {

    @GetMapping("/wallet/getBalance/{id}")
    public Long getBalance(@PathVariable("id") Long id);

    @PostMapping("wallet/createwallet/{id}")
    public Boolean createWallet(@PathVariable Long id);


}