package com.payment.controler;

import com.payment.advice.NegativeValueException;
import com.payment.clients.EmailSenderINF;
import com.payment.clients.TransactionINF;
import com.payment.clients.WalletINF;
import com.payment.entities.Transactions;
import com.payment.entities.TransferRequestDTO;
import com.payment.entities.Wallet;
import com.payment.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payment")
public class paymentController {
    @Autowired
    private PaymentService paymentService;
    @PostMapping("/transfer")
    public Boolean transferAmount(@RequestBody TransferRequestDTO transferRequestDTO,  HttpServletRequest request){
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
        if(transferRequestDTO.getBalance()<0){
            throw new NegativeValueException("Amount can't be negative");
        }
        System.out.println("paymentController  transferAmount  2 ************** " + transferRequestDTO);
        transferRequestDTO.setId(id);
        if(transferRequestDTO.getId() == transferRequestDTO.getReceiverId()){
            throw new RuntimeException("Same Id ");
        }
        paymentService.transfer(transferRequestDTO);
        return true;
    }
    @PostMapping("/credit")
    public Boolean creditAmount(@RequestBody TransferRequestDTO transferRequestDTO, HttpServletRequest request){
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
        transferRequestDTO.setId(id);
        if(transferRequestDTO.getBalance()<0){
            throw new NegativeValueException("Amount can't be negative");
        }

        paymentService.credit(transferRequestDTO);
        return true;
    }

    @PostMapping("/debit")
    public Boolean debitAmount(@RequestBody TransferRequestDTO transferRequestDTO, HttpServletRequest request ){
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
        transferRequestDTO.setId(id);
        if(transferRequestDTO.getBalance()<0){
            throw new NegativeValueException("Amount can't be negative");
        }

        paymentService.debit(transferRequestDTO);

        return true;
    }

    @GetMapping("/alltransactions/{id}" )
    public List<Transactions> getAllTransactionOfUser(@PathVariable Long id, HttpServletRequest request){
        String userIdHeader = request.getHeader("X-User-Id");
        if (userIdHeader == null) {
            throw new RuntimeException("Missing X-User-Id header");
        }

        Long id1;
        try {
            id1 = Long.valueOf(userIdHeader);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Invalid X-User-Id header format");
        }
        System.out.println("id1: " + id1);
        if(id1 != id){
            throw new RuntimeException("Invalid id");
        }

        return paymentService.getAllTransactionOfUser(id);
    }






}
