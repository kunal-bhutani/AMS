package com.jocata.AMS.service.impl;

import com.jocata.AMS.dao.AccountDao;
import com.jocata.AMS.dao.UserDao;
import com.jocata.AMS.entity.Account;
import com.jocata.AMS.entity.User;
import com.jocata.AMS.enums.AccountStatus;
import com.jocata.AMS.enums.AccountType;
import com.jocata.AMS.forms.AccountForm;
import com.jocata.AMS.service.AccountService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {
    private final AccountDao accountDao;
    private final UserDao userDao;

    public AccountServiceImpl(AccountDao accountDao, UserDao userDao) {
        this.accountDao = accountDao;
        this.userDao = userDao;
    }

    @Override
    public AccountForm create(AccountForm account) {
        Account account1 = accountDao.create(accountFormToEntity(account));
        return entityToAccountForm(account1);
    }

    @Override
    public AccountForm getAccountById(Integer accountId) {
        Account accountById = accountDao.getAccountById(accountId);
        return entityToAccountForm(accountById);
    }

    @Override
    public AccountForm updateAccountStatus(Integer id , String status) {
        Account accountById = accountDao.getAccountById(id);
        accountById.setStatus(AccountStatus.valueOf(status));
        Account account1 = accountDao.updateAccountStatus(accountById);
        return entityToAccountForm(account1);
    }

    @Override
    public String accountBalance(Integer accountId) {
        Account accountById = accountDao.getAccountById(accountId);
        return accountById.getBalance().toString();
    }

    @Override
    public List<AccountForm> getAllAccounts() {
        List<Account> accounts = accountDao.getAllAccounts();
        List<AccountForm> accountForms = new ArrayList<>();
        for (Account account : accounts) {
            accountForms.add(entityToAccountForm(account));
        }
        return accountForms;
    }

    private Account accountFormToEntity(AccountForm accountForm) {
        Account account = new Account();
        User userByEmail = userDao.getUserByEmail(accountForm.getEmail());
        account.setUser(userByEmail);
        account.setAccountNumber(accountForm.getAccountNumber());
        account.setAccountType(AccountType.valueOf(accountForm.getAccountType()));
        account.setBalance(new BigDecimal(accountForm.getBalance()));
        if(accountForm.getStatus() != null) {
            account.setStatus(AccountStatus.valueOf(accountForm.getStatus()));
        }
        return account;
    }

    private AccountForm entityToAccountForm(Account account) {
        AccountForm accountForm = new AccountForm();
        accountForm.setId(String.valueOf(account.getId()));
        accountForm.setAccountNumber(account.getAccountNumber());
        accountForm.setAccountType(String.valueOf(account.getAccountType()));
        accountForm.setBalance(account.getAccountNumber());
        accountForm.setStatus(String.valueOf(account.getStatus()));
        accountForm.setEmail(account.getUser().getEmail());
        accountForm.setCreatedAt(account.getCreatedAt().toString());
        accountForm.setUpdatedAt(account.getUpdatedAt().toString());
        return accountForm;
    }
}
