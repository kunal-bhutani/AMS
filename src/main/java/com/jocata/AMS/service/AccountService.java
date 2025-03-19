package com.jocata.AMS.service;

import com.jocata.AMS.enums.AccountStatus;
import com.jocata.AMS.forms.AccountForm;

import java.util.List;

public interface AccountService {

    AccountForm create(AccountForm account);
    AccountForm getAccountById(Integer accountId);
    AccountForm updateAccountStatus(Integer accountId, String status);
    String accountBalance(Integer accountId);
    List<AccountForm> getAllAccounts();
}
