package com.bank.client;

import com.bank.config.FeignConfig;
import com.bank.entity.*;
import org.springframework.boot.autoconfigure.pulsar.PulsarProperties;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(url = "http://localhost:8090", value = "apiClient" , configuration = FeignConfig.class)
public interface ApiClient {

    @PostMapping("/bank/register")
    public ResponseEntity<String> userCreate(@RequestBody UserDto userDto);


    @PostMapping("/bank/login")
    public ResponseEntity<LoginResponse> authenticateUser(@RequestBody LoginRequest loginRequest);

    @PostMapping("/payment/credit")
    Boolean creditAmount(@RequestBody TransferRequestDTO transferRequestDTO);

    @PostMapping("/payment/debit")
    Boolean debitAmount(@RequestBody TransferRequestDTO transferRequestDTO);

    @GetMapping("/wallet/getBalance")
    Long getBalance();

    @GetMapping("/transaction/getAllById")
    List<Transactions> getAllTransactions();

    @PostMapping("/payment/transfer")
    Boolean transferAmount(@RequestBody TransferRequestDTO transferRequestDTO);

    @GetMapping("/user/getUserDetails")
    public ResponseEntity<User> getUser();

    @PutMapping("/user/edit")
    public Boolean edit(@RequestBody User user);
}
