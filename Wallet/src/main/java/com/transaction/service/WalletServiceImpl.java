package com.transaction.service;

import com.transaction.entity.CreditDebitReq;
import com.transaction.entity.TransferRequestDTO;
import com.transaction.entity.Wallet;
import com.transaction.repository.WalletRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class WalletServiceImpl implements WalletInf {

    @Autowired
    private WalletRepo repo;


    @Override
    public Wallet save(Wallet transactions) {
        return repo.save(transactions);
    }


    @Override
    public Long getBalance(Long id) {
         Wallet wallet = repo.findByUserid(id);
        if (wallet == null) {
            throw new IllegalArgumentException("Wallet not found for userId: " + id);
        }
         return wallet.getBalance();

    }

    @Override
    public Boolean isActive(Long id)
    {
        Wallet wallet = repo.findByUserid(id);
        if (wallet == null) {
            throw new IllegalArgumentException("Wallet not found for userId: " + id);
        }
        return wallet.isActive();
    }

    @Override
    public List<Wallet> getAll() {
        return repo.findAll();
    }

    @Override
    public Boolean credit(CreditDebitReq creditDebitReq) {
        Wallet wallet = repo.findByUserid(creditDebitReq.getUserid());
        if (wallet == null) {
            throw new IllegalArgumentException("Wallet not found for userId: " + creditDebitReq.getUserid());
        }
        Long balance = wallet.getBalance();
        wallet.setBalance(balance+creditDebitReq.getBalance());
        Wallet save = repo.save(wallet);

        return true;
    }

    @Override
    public Boolean debit(CreditDebitReq creditDebitReq) {
        Wallet wallet = repo.findByUserid(creditDebitReq.getUserid());
        if (wallet == null) {
            throw new IllegalArgumentException("Wallet not found for userId: " + creditDebitReq.getUserid());
        }
        Long balance = wallet.getBalance();
        wallet.setBalance(balance-creditDebitReq.getBalance());
        Wallet save = repo.save(wallet);

        return true;
    }

    @Override
    public Wallet getWallet(Long id) {
        Wallet wallet = repo.findByUserid(id);
        if (wallet == null) {
            throw new IllegalArgumentException("Wallet not found for userId: " + id);
        }
        return wallet;
    }

    @Override
    public void transfer(TransferRequestDTO transferRequestDTO) {
        System.out.println("Sender id: "+transferRequestDTO.getId());
        System.out.println("receiver id: "+transferRequestDTO.getReceiverId());
        Wallet senderWallet = repo.findByUserid(transferRequestDTO.getId());
        Wallet receiverWallet = repo.findByUserid(transferRequestDTO.getReceiverId());
//        if(senderWallet==null || receiverWallet==null){
//            //error
//        }
//        if(!senderWallet.isActive() ||!receiverWallet.isActive() ){
//            // error
//        }
//        if(senderWallet.getBalance()<transferRequestDTO.getBalance()){
//            // error
//        }

        senderWallet.setBalance(senderWallet.getBalance()-transferRequestDTO.getBalance());
        receiverWallet.setBalance(receiverWallet.getBalance()+transferRequestDTO.getBalance());
        repo.save(senderWallet);
        repo.save(receiverWallet);

    }

    @Override
    public void createWalllet(Long id) {
        Wallet wallet = new Wallet();
        wallet.setActive(true);
        wallet.setUserid(id);
        wallet.setBalance(0L);
        repo.save(wallet);
    }


}
