package com.jocata.AMS.service.impl;

import com.jocata.AMS.dao.AccountDao;
import com.jocata.AMS.dao.TransactionDao;
import com.jocata.AMS.entity.Account;
import com.jocata.AMS.entity.Transaction;
import com.jocata.AMS.enums.TransactionType;
import com.jocata.AMS.forms.TransactionForm;
import com.jocata.AMS.forms.TransactionRequestForm;
import com.jocata.AMS.service.TransactionService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {
    private final TransactionDao transactionDao;
    private final AccountDao accountDao;

    public TransactionServiceImpl(TransactionDao transactionDao, AccountDao accountDao) {
        this.transactionDao = transactionDao;
        this.accountDao = accountDao;
    }

    @Override
    public TransactionForm deposit(TransactionRequestForm form) {
        Transaction transaction = transactionDao.create(depositCharge(form));
        return entityToForm(transaction);
    }

    @Override
    public TransactionForm withdraw(TransactionRequestForm form) {
        Transaction transaction = withdrawCharge(form);
        System.out.println("Transaction withdrawn idd: " + transaction.getId());
        return entityToFormWithdraw(transaction);
    }

    @Override
    public List<TransactionForm> getTransactions(Integer accountId) {
        List<Transaction> transactionsForAccount = transactionDao.getTransactionsForAccount(accountId);
        List<TransactionForm> transactionForms = new ArrayList<>();
        for (Transaction transaction : transactionsForAccount) {
            transactionForms.add(entityToForm(transaction));
        }
        return transactionForms;
    }

    @Override
    public List<TransactionForm> getAllTransactions() {
        List<Transaction> allTransactions = transactionDao.getAllTransactions();
        List<TransactionForm> transactionForms = new ArrayList<>();
        for (Transaction transaction : allTransactions) {
            transactionForms.add(entityToForm(transaction));
        }
        return transactionForms;
    }


    private Transaction depositCharge(TransactionRequestForm form) {
        Transaction transaction = new Transaction();
        Account accountById = accountDao.getAccountById(Integer.valueOf(form.getAccountId()));
        if (accountById == null) {
            System.out.println("Account with id " + form.getAccountId() + " not found");
        }
        transaction.setAccount(accountById);
        transaction.setAmount(new BigDecimal(form.getAmount()));
        transaction.setTransactionType(TransactionType.DEPOSIT);
        BigDecimal amount = new BigDecimal(form.getAmount());
        BigDecimal totalBalance = amount.add(accountById.getBalance());
        transaction.setCurrentBalance(totalBalance);
        accountById.setBalance(totalBalance);
        accountDao.create(accountById);
        return transaction;

    }

    private Transaction withdrawCharge(TransactionRequestForm form) {

        Transaction transaction = new Transaction();
        Account accountById = accountDao.getAccountById(Integer.valueOf(form.getAccountId()));
        BigDecimal balance = accountById.getBalance();

        BigDecimal charge = new BigDecimal(0);
        BigDecimal amount = new BigDecimal(form.getAmount());

        if (amount.compareTo(new BigDecimal(10000)) > 0) {
            charge = amount.multiply(new BigDecimal(0.01));
        }
        if (!form.getBank().equals("bankabc")) {
            charge = charge.add(new BigDecimal(2));
        }
        if (charge.compareTo(new BigDecimal(10)) < 0) {
            charge = new BigDecimal(10);
        } else if (charge.compareTo(new BigDecimal(1000)) > 0) {
            charge = new BigDecimal(1000);
        }
        if (!accountById.getUser().getNationality().toLowerCase().equals("indian")) {
            charge = charge.add(charge.multiply(new BigDecimal(0.05)));
        }
        if (balance.compareTo(amount) < 0) {
            throw new RuntimeException("Low Balance.");
        }
        accountById.setBalance(balance.subtract(amount));
        Account updateAccount = accountDao.create(accountById);
        transaction.setCurrentBalance(accountById.getBalance().subtract(amount));
        amount = amount.add(charge);
        transaction.setTransactionType(TransactionType.WITHDRAWAL);
        transaction.setAmount(amount);
        transaction.setAccount(updateAccount);
        transaction.setCharge(charge);
        Transaction transaction1 = transactionDao.create(transaction);
        return transaction1;


    }

    private TransactionForm entityToForm(Transaction transaction) {
        TransactionForm form = new TransactionForm();
        form.setId(String.valueOf(transaction.getId()));
        form.setAmount(String.valueOf(transaction.getAmount()));
        form.setAccountId(String.valueOf(transaction.getAccount().getId()));
        form.setCharge(String.valueOf(transaction.getTransactionType()));
        form.setCurrentBalance(String.valueOf(transaction.getCurrentBalance()));
        form.setTimestamp(String.valueOf(transaction.getTransactionTimestamp()));
        form.setDateOfTransaction(String.valueOf(transaction.getDateOfTransaction()));
        return form;
    }

    private TransactionForm entityToFormWithdraw(Transaction transaction) {
        TransactionForm form = new TransactionForm();
        form.setId(String.valueOf(transaction.getId()));
        form.setAmount(String.valueOf(transaction.getAmount()));
        form.setAccountId(String.valueOf(transaction.getAccount().getId()));
        form.setCharge(String.valueOf(transaction.getTransactionType()));
        form.setCurrentBalance(String.valueOf(transaction.getCurrentBalance()));
        form.setTimestamp(String.valueOf(transaction.getTransactionTimestamp()));
        form.setDateOfTransaction(String.valueOf(transaction.getDateOfTransaction()));

        return form;
    }

}
