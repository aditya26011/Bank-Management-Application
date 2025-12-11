package com.payment.service;

import com.payment.clients.EmailSenderINF;
import com.payment.clients.TransactionINF;
import com.payment.clients.WalletINF;
import com.payment.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentService {
    @Autowired
    private WalletINF walletINF;
    @Autowired
    private TransactionINF transactionINF;
    @Autowired
    private EmailSenderINF emailSenderINF;


    public void transfer(TransferRequestDTO transferRequestDTO){


        System.out.println("PaymentService  transfer  3 ************** ");
        Long balance = walletINF.getBalance(transferRequestDTO.getId());
        if(balance< transferRequestDTO.getBalance()){
            throw new RuntimeException("Insufficient balance");
        }

        walletINF.transfer(transferRequestDTO);


        System.out.println("PaymentService  transfer  4 ************** ");
        transactionINF.save(transferRequestDTO);



        // will work on email pemplates
        //emailSenderINF.sendEmail()


    }

    public void credit(TransferRequestDTO transferRequestDTO) {
        CreditDebitReq creditDebitReq = new CreditDebitReq();
        creditDebitReq.setUserid(transferRequestDTO.getId());
        creditDebitReq.setBalance(transferRequestDTO.getBalance());

        walletINF.credit(creditDebitReq);
        transferRequestDTO.setType(Type.CREDIT);

        transactionINF.save(transferRequestDTO);

        // will work on email pemplates
        //emailSenderINF.sendEmail()

    }

    public void debit(TransferRequestDTO transferRequestDTO) {
        CreditDebitReq creditDebitReq = new CreditDebitReq();
        creditDebitReq.setUserid(transferRequestDTO.getId());
        creditDebitReq.setBalance(transferRequestDTO.getBalance());
        Long balance = walletINF.getBalance(transferRequestDTO.getId());
        if(balance< transferRequestDTO.getBalance()){
            throw new RuntimeException("Insufficient balance");
        }
        walletINF.debit(creditDebitReq);
        transferRequestDTO.setType(Type.DEBIT);



        transactionINF.save(transferRequestDTO);

        // will work on email pemplates
        //emailSenderINF.sendEmail()
    }

    public List<Transactions> getAllTransactionOfUser(Long id) {
        return transactionINF.getAllByUserId(id);
    }
}
