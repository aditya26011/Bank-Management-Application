package com.user.userservice.client;

import com.user.userservice.entity.Transactions;
import com.user.userservice.entity.TransferRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(url= "http://localhost:8084", value = "payment-client")

public interface PaymentINF {

    @PostMapping("/payment/transfer")
    public Boolean transferAmount(@RequestBody TransferRequestDTO transferRequestDTO);

    @PostMapping("/payment/credit")
    public Boolean creditAmount(@RequestBody TransferRequestDTO transferRequestDTO);

    @PostMapping("/payment/debit")
    public Boolean debitAmount(@RequestBody TransferRequestDTO transferRequestDTO);

    @GetMapping("/payment/alltransactions/{id}")
    public List<Transactions> getAllTransactionsOfUser(@PathVariable("id") Long id);
}
