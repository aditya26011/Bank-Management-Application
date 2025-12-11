package com.transaction.controller;

import com.transaction.entity.Transactions;
import com.transaction.entity.TransferRequestDTO;
import com.transaction.service.TransactionInf;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    @Autowired
    private TransactionInf service;

    @GetMapping("/getAll")
    public List<Transactions> getAll(){
        return  service.getAll();
    }

    @GetMapping("/getAllById")
    public List<Transactions> getAllByUserId(HttpServletRequest request)
    {
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
        return  service.getById(id);
    }
    @PostMapping("/save")
    public Transactions save(@RequestBody TransferRequestDTO transferRequestDTO){
        System.out.println("TransactionController save  ****** "+transferRequestDTO);
        return  service.save(transferRequestDTO);
    }


}
