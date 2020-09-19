package cn.sucrelt.dao;

import cn.sucrelt.domain.Account;

import java.util.List;

/**
 * @description:账户的持久层接口
 * @author: sucre
 * @date: 2020/09/19
 * @time: 16:19
 */
public interface AccountDao {
    /**
     * 查询所有
     *
     * @return
     */
    List<Account> findAllAccount();

    /**
     * 根据id查询一个账户
     *
     * @return
     */
    Account findAccountById(Integer accountId);


    /**
     * 保存账户信息
     *
     * @param account
     */
    void saveAccount(Account account);

    /**
     * 更新账户信息
     *
     * @param account
     */
    void updateAccount(Account account);


    /**
     * 删除账户信息
     *
     * @param accountId
     */
    void deleteAccount(Integer accountId);
}
