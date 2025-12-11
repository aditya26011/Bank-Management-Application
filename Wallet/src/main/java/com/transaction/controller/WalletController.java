package com.transaction.controller;

import com.transaction.entity.CreditDebitReq;
import com.transaction.entity.TransferRequestDTO;
import com.transaction.entity.Wallet;
import com.transaction.service.WalletInf;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wallet")
public class WalletController {

    @Autowired
    private WalletInf service;

    @GetMapping("/getAll")
    public List<Wallet> getAll(){
        return  service.getAll();
    }

    @GetMapping("/getBalance")
    public Long getBalance(HttpServletRequest request){
        String userIdHeader = request.getHeader("X-User-Id");
        if (userIdHeader == null) {
            throw new RuntimeException("Missing X-User-Id header");
        }

        Long id;
        try {
            id = Long.valueOf(userIdHeader);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Invalid X-User-Id header format");
        }

        return  service.getBalance(id);
    }

    @GetMapping("/isActive/{id}")
    public Boolean isActive(@PathVariable Long id){
        return service.isActive(id);
    }
    @PostMapping("/save")
    public Wallet save(@RequestBody Wallet transactions){
        return  service.save(transactions);
    }

    @PostMapping("/credit")
    public Boolean credit(@RequestBody CreditDebitReq creditDebitReq){
        return service.credit(creditDebitReq);
    }

    @PostMapping("/debit")
    public Boolean debit(@RequestBody CreditDebitReq creditDebitReq){
        return service.debit(creditDebitReq);
    }

    @GetMapping("/{id}")
    public Wallet getWallet(@PathVariable Long id){
        return  service.getWallet(id);

    }

    @PostMapping("/transfer")
    public Boolean transfer(@RequestBody TransferRequestDTO transferRequestDTO){
        service.transfer(transferRequestDTO);
        return true;

    }

    @PostMapping("/createwallet/{id}")
    public Boolean createWallet(@PathVariable Long id){
        service.createWalllet(id);
        return true;
    }


}
