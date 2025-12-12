package com.transaction.service;

import com.transaction.client.NotifyINF;
import com.transaction.client.UserINF;
import com.transaction.entity.*;
import com.transaction.repository.TransactionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//import java.time.LocalDate;
//import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.util.List;

@Service

public class TransactionServiceImpl implements TransactionInf{

    @Autowired
    private TransactionRepo repo;

    @Autowired
    UserINF userINF;

    @Autowired
    NotifyINF notifyINF;




    @Override
    @Transactional
    public Transactions save(TransferRequestDTO transferRequestDTO) {
        EmailDTO emailDTO = new EmailDTO();

        // Case 1: Own account transaction (credit/debit)
        if (transferRequestDTO.getReceiverId() == null) {
            Transactions transactions = new Transactions();
            transactions.setUserId(transferRequestDTO.getId());
            transactions.setSender("SELF");
            transactions.setBalance(transferRequestDTO.getBalance());
            transactions.setType(transferRequestDTO.getType());
            transactions.setStatus(Status.SUCCESSFUL);


            // Save transaction
            Transactions savedTransaction = repo.save(transactions);

//             Prepare email
            String userEmail = userINF.getEmail(transactions.getUserId());
            emailDTO.setTo(userEmail);
            emailDTO.setSubject("Transaction Successful");
            emailDTO.setBody(
                    "Dear User,\n\n" +
                            "Your transaction has been successfully processed.\n" +
                            "Details:\n" +
                            "Type: " + transactions.getType() + "\n" +
                            "Amount: ₹" + transactions.getBalance() + "\n" +
                            "Status: " + transactions.getStatus() + "\n" +
                            "Transaction ID: " + savedTransaction.getId() + "\n" +
                            "Date: " + LocalDateTime.now() + "\n\n" +
                            "Thank you for banking with us."
            );

            notifyINF.sendEmail(emailDTO);
            return savedTransaction;

        } else {
            String reveiverUsername = userINF.getusername(transferRequestDTO.getReceiverId()).getBody();
            String senderUsername = userINF.getusername(transferRequestDTO.getId()).getBody();
            // Case 2: Transfer between accounts
            // Sender transaction
            Transactions senderTransaction = new Transactions();
            senderTransaction.setUserId(transferRequestDTO.getId());


            senderTransaction.setSender("SELF");
            senderTransaction.setReceiver(reveiverUsername);
            senderTransaction.setBalance(transferRequestDTO.getBalance());
            senderTransaction.setType(Type.DEBIT);
            senderTransaction.setStatus(Status.SUCCESSFUL);
            repo.save(senderTransaction);

            // Receiver transaction
            Transactions receiverTransaction = new Transactions();
            receiverTransaction.setUserId(transferRequestDTO.getReceiverId());
            receiverTransaction.setReceiver("SELF");
            receiverTransaction.setSender(senderUsername);
            receiverTransaction.setBalance(transferRequestDTO.getBalance());
            receiverTransaction.setType(Type.CREDIT);
            receiverTransaction.setStatus(Status.SUCCESSFUL);
            Transactions savedReceiverTransaction = repo.save(receiverTransaction);

            // Prepare email for sender
            String senderEmail = userINF.getEmail(senderTransaction.getUserId());
            emailDTO.setTo(senderEmail);
            emailDTO.setSubject("Amount Transferred Successfully");
            emailDTO.setBody(
                    "Dear User,\n\n" +
                            "You have successfully transferred ₹" + transferRequestDTO.getBalance() +
                            " to User ID: " + transferRequestDTO.getReceiverId() + ".\n" +
                            "Status: SUCCESSFUL\n" +
                            "Transaction ID: " + senderTransaction.getId() + "\n" +
                            "Date: " + LocalDateTime.now() + "\n\n" +
                            "Thank you for banking with us."
            );

            notifyINF.sendEmail(emailDTO);
            // Prepare email for receiver (optional)
            EmailDTO receiverEmailDTO = new EmailDTO();
            receiverEmailDTO.setTo(userINF.getEmail(receiverTransaction.getUserId()));
            receiverEmailDTO.setSubject("Amount Credited to Your Account");
            receiverEmailDTO.setBody(
                    "Dear User,\n\n" +
                            "You have received ₹" + transferRequestDTO.getBalance() +
                            " from User ID: " + transferRequestDTO.getId() + ".\n" +
                            "Status: SUCCESSFUL\n" +
                            "Transaction ID: " + receiverTransaction.getId() + "\n" +
                            "Date: " + LocalDateTime.now() + "\n\n" +
                            "Thank you for banking with us."
            );

            notifyINF.sendEmail(emailDTO);
            return savedReceiverTransaction;
        }
    }



    @Override
    public List<Transactions> getById(Long id) {
         return repo.findAllByUserId(id);

    }

    @Override
    public List<Transactions> getAll() {
        return repo.findAll();
    }
}
