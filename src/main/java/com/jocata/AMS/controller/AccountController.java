package com.jocata.AMS.controller;

import com.jocata.AMS.forms.AccountForm;
import com.jocata.AMS.service.AccountService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/account/create")
    public AccountForm createAccount(@RequestBody AccountForm accountForm) {
        AccountForm accountForm1 = accountService.create(accountForm);
        if(accountForm1 != null) {
            System.out.println("account crated successfully");
            return accountForm1;
        }else {
            System.out.println("fail account creation");
            return null;
        }
    }
    @GetMapping("/account/{id}")
    public AccountForm getAccount(@PathVariable("id") String id) {
        return accountService.getAccountById(Integer.valueOf(id));
    }
    @GetMapping("/account/balance/{id}")
        public String getAccountBalance(@PathVariable("id") String id){
        return accountService.accountBalance(Integer.valueOf(id));
    }
    @GetMapping("/admin/accounts")
    public List<AccountForm> getAllAccounts() {
        return accountService.getAllAccounts();
    }
    @PutMapping("/admin/account/status/{id}")
    public AccountForm updateAccountStatus(@PathVariable("id") String id, @RequestParam String status) {
        return accountService.updateAccountStatus(Integer.valueOf(id), status);
    }
}
