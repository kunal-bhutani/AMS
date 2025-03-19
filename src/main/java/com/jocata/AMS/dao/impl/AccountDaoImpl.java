package com.jocata.AMS.dao.impl;

import com.jocata.AMS.config.HibernateConfig;
import com.jocata.AMS.dao.AccountDao;
import com.jocata.AMS.entity.Account;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.List;


@Repository
public class AccountDaoImpl implements AccountDao {
    private final HibernateConfig hibernateConfig;

    public AccountDaoImpl(HibernateConfig hibernateConfig) {
        this.hibernateConfig = hibernateConfig;
    }

    @Override
    public Account create(Account account) {
        return hibernateConfig.saveOrUpdateEntity(account);
    }

    @Override
    public Account getAccountById(Integer accountId) {
        return hibernateConfig.findEntityById(Account.class, accountId);
    }

    @Override
    public Account updateAccountStatus(Account account) {
        return hibernateConfig.saveOrUpdateEntity(account);
    }

    @Override
    public List<Account> getAllAccounts() {
        return hibernateConfig.loadEntitiesByCriteria(Account.class);
    }
}
