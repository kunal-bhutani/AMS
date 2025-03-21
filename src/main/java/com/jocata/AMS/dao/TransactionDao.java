package com.jocata.AMS.dao;

import com.jocata.AMS.entity.Transaction;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionDao {
    Transaction create(Transaction transaction);
    List<Transaction> getTransactionsForAccount(Integer accountId);
    List<Transaction> getAllTransactions();

}
