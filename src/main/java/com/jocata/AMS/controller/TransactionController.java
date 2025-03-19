package com.jocata.AMS.controller;

import com.jocata.AMS.entity.Transaction;
import com.jocata.AMS.forms.TransactionForm;
import com.jocata.AMS.forms.TransactionRequestForm;
import com.jocata.AMS.service.TransactionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/account/deposit")
    public TransactionForm deposit(@RequestBody TransactionRequestForm form) {
       return transactionService.deposit(form);
    }
    @PostMapping("/account/withdraw")
    public TransactionForm withdraw(@RequestBody TransactionRequestForm form) {
        return transactionService.withdraw(form);
    }
    @GetMapping("/transactions/{accountId}")
    public List<TransactionForm> getTransactions(@PathVariable String accountId) {
        return transactionService.getTransactions(Integer.valueOf(accountId));
    }

    @GetMapping("/admin/transactions")
    public List<TransactionForm> getAllTransactions() {
        return transactionService.getAllTransactions();
    }

}
