package cn.sucrelt.test;

import cn.sucrelt.domain.Account;
import cn.sucrelt.service.AccountService;
import config.JdbcConfig;
import config.SpringConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


import java.util.List;

/**
 * @description:使用JUnit单元测试测试配置内容
 * @author: sucre
 * @date: 2020/09/19
 * @time: 16:39
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringConfiguration.class)
public class AccountServiceTest {

    @Autowired
    private AccountService accountService;

    // ApplicationContext applicationContext = new ClassPathXmlApplicationContext("bean.xml");

    //使用注解开发
    // ApplicationContext applicationContext = new AnnotationConfigApplicationContext(SpringConfiguration.class);
    // AccountService accountService = applicationContext.getBean("accountService", AccountService.class);

    @Test
    public void testFindAllAccount() {
        List<Account> accounts = accountService.findAllAccount();
        for (Account account :
                accounts) {
            System.out.println(account.toString());
        }
    }

    @Test
    public void testFindAccountById() {
        Account account = accountService.findAccountById(1);
        System.out.println(account);
    }

    @Test
    public void testSaveAccount() {
        Account account = new Account();
        account.setName("测试用户");
        account.setMoney(12345f);

        accountService.saveAccount(account);
    }

    @Test
    public void testUpdateAccount() {
        Account account = accountService.findAccountById(4);
        account.setMoney(23456f);
        accountService.updateAccount(account);
    }

    @Test
    public void testDeleteAccount() {
        accountService.deleteAccount(6);
    }

}
