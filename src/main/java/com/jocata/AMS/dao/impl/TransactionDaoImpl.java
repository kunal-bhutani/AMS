package com.jocata.AMS.dao.impl;

import com.jocata.AMS.config.HibernateConfig;
import com.jocata.AMS.dao.TransactionDao;
import com.jocata.AMS.entity.Transaction;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class TransactionDaoImpl implements TransactionDao {
    private final HibernateConfig hibernateConfig;

    public TransactionDaoImpl(HibernateConfig hibernateConfig) {
        this.hibernateConfig = hibernateConfig;
    }

    @Override
    public Transaction create(Transaction transaction) {
        return hibernateConfig.saveOrUpdateEntity(transaction);
    }

    @Override
    public List<Transaction> getTransactionsForAccount(Integer accountId) {
        return hibernateConfig.findEntitiesByCriteria(Transaction.class, "account.id", accountId);
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return hibernateConfig.loadEntitiesByCriteria(Transaction.class);
    }

    @Override
    public List<Transaction> getTransactionsBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        Timestamp startTimestamp = Timestamp.valueOf(startDate);
        Timestamp endTimestamp = Timestamp.valueOf(endDate);

        return hibernateConfig.getEntitiesBetweenDates(Transaction.class, "timestamp", startTimestamp, endTimestamp);
    }

    @Override
    public String generateTransactionPdf(LocalDateTime startDate, LocalDateTime endDate) {
        return "";
    }
}
