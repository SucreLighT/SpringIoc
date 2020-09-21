package config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.springframework.context.annotation.*;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;

/**
 * @description:注解开发中的配置类，等同于bean.xml
 * @author: sucre
 * @date: 2020/09/21
 * @time: 09:33
 */

@Configuration
@ComponentScan(basePackages = "cn.sucrelt")
@Import(JdbcConfig.class)
public class SpringConfiguration {
}
