package com.payment.clients;

import com.payment.entities.CreditDebitReq;
import com.payment.entities.Transactions;
import com.payment.entities.TransferRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(url= "https://transaction-service-141f.onrender.com", value = "transaction-client")
public interface TransactionINF {

    @GetMapping("/transaction/getAllById/{id}")
    public List<Transactions> getAllByUserId(@PathVariable("id") Long id);
    @PostMapping("/transaction/save")
    public Transactions save(@RequestBody TransferRequestDTO transactions);
    @PostMapping("/transaction/saveAll")
    public List<Transactions> save(@RequestBody List<Transactions> transactions);

}
