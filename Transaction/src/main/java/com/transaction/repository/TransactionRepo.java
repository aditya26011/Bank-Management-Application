package com.transaction.repository;

import com.transaction.entity.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepo extends JpaRepository<Transactions, Long> {
    List<Transactions> findAllByUserId(Long userId);

}
