package cn.sucrelt.service.impl;

import cn.sucrelt.dao.AccountDao;
import cn.sucrelt.domain.Account;
import cn.sucrelt.service.AccountService;

import java.util.List;

/**
 * @description:账户的业务层实现类
 * @author: sucre
 * @date: 2020/09/19
 * @time: 16:17
 */
public class AccountServiceImpl implements AccountService {

    private AccountDao accountDao;

    public void setAccountDao(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    public List<Account> findAllAccount() {
        return accountDao.findAllAccount();
    }

    public Account findAccountById(Integer accountId) {
        return accountDao.findAccountById(accountId);
    }

    public void saveAccount(Account account) {
        accountDao.saveAccount(account);
    }

    public void updateAccount(Account account) {
        accountDao.updateAccount(account);
    }

    public void deleteAccount(Integer accountId) {
        accountDao.deleteAccount(accountId);
    }
}
