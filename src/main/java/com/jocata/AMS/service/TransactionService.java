package com.jocata.AMS.service;

import com.jocata.AMS.entity.Transaction;
import com.jocata.AMS.forms.TransactionForm;
import com.jocata.AMS.forms.TransactionRequestForm;

import java.io.OutputStream;
import java.util.List;

public interface TransactionService {
    public TransactionForm deposit(TransactionRequestForm form);
    public TransactionForm withdraw(TransactionRequestForm form);
    List<TransactionForm> getTransactions(Integer accountId);
    List<TransactionForm> getAllTransactions();
    public void generateTransactionPdf(Integer accountId, Integer month, OutputStream outputStream);
}
