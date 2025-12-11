package com.transaction.service;

import com.transaction.entity.Transactions;
import com.transaction.entity.TransferRequestDTO;

import java.util.List;

public interface TransactionInf
{
    Transactions save(TransferRequestDTO transferRequestDTO);


    List<Transactions> getById(Long id);
    public List<Transactions> getAll();

}
