package com.jocata.AMS.dao;

import com.jocata.AMS.entity.Account;

import java.math.BigDecimal;
import java.util.List;

public interface AccountDao {
    Account create(Account account);
    Account getAccountById(Integer accountId);
    Account updateAccountStatus(Account account);
    List<Account> getAllAccounts();

}
