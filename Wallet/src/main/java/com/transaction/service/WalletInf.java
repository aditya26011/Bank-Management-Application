package com.transaction.service;

import com.transaction.entity.CreditDebitReq;
import com.transaction.entity.TransferRequestDTO;
import com.transaction.entity.Wallet;

import java.util.List;

public interface WalletInf
{
    Wallet save(Wallet wallet);


    Long getBalance(Long id);
    Boolean isActive(Long id);

    List<Wallet> getAll();
    Boolean credit(CreditDebitReq creditDebitReq);

    Boolean debit(CreditDebitReq creditDebitReq);

    Wallet getWallet(Long id);

    void transfer(TransferRequestDTO transferRequestDTO);

    void createWalllet(Long id);
}
