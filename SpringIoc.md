## Spring概述

Spring是分层的Java SE/EE应用full-stack轻量级开源框架，以IoC（Inverse Of Control：反转控制）和AOP（Aspect Oriented Programming：面向切面编程）为内核，提供了表现层Spring MVC和持久层Spring JDBC以及业务层事务管理等众多的企业级应用技术。

### Spring优势

##### 1.方便解耦，简化开发

通过Spring提供的IoC容器，可以将对象间的依赖关系交由Spring进行控制，避免硬编码所造成的过度程序耦合。用户也不必再为单例模式类、属性文件解析等这些很底层的需求编写代码，可以更专注于上层的应用。

##### 2.AOP编程的支持

通过Spring的AOP功能，方便进行面向切面的编程，许多不容易用传统OOP实现的功能可以通过AOP轻松应付。

##### 3.声明式事务的支持

可以将我们从单调烦闷的事务管理代码中解脱出来，通过声明式方式灵活的进行事务的管理，提高开发效率和质量。

##### 4.方便程序的测试

可以用非容器依赖的编程方式进行几乎所有的测试工作，测试不再是昂贵的操作，而是随手可做的事情。

##### 5.方便集成各种优秀框架

Spring可以降低各种框架的使用难度，提供了对各种优秀框架（Struts、Hibernate、Hessian、Quartz等）的直接支持。

##### 6.降低JavaEE API的使用难度

Spring对JavaEE API（如JDBC、JavaMail、远程调用等）进行了薄薄的封装层，使这些API的使用难度大为降低。



### 程序的耦合性

1. 模块间的耦合度是指模块之间的依赖关系，包括控制关系、调用关系、数据传递关系。模块间联系越多，其耦合性越强，同时表明其独立性越差。
2. 在开发过程中需要降低程序间的依赖关系，做到：**编译期不依赖，运行时才依赖**。
3. JDBC操作注册驱动时，不使用DriverManager的register方法，而是采用Class.forName的反射方式来注册驱动。
   + **其好处就是**：类中不再依赖具体的驱动类，此时就算删除mysql的驱动jar包，依然可以编译。
   + 同时，也产生了一个新的问题：mysql驱动的全限定类名字符串是在java类中写死的，如果要修改（如换成oracle数据库）还是要修改源码，解决这个问题的方法是使用配置文件配置。
4. 使用工厂模式可以实现解耦：在获取对象时，不再是使用new关键字的主动创建，而是通过工厂类被动获取对象。**这种被动接收的方式获取对象的思想就是控制反转IOC**。



## Spring-IOC

**控制反转IOC**：对象的创建交给外部容器完成
Spring使⽤控制反转来实现对象不⽤在程序中写死，解决对象处理问题【把对象交给别⼈创建】
**依赖注⼊dependency injection**：对象之间的依赖关系的实现

控制反转是通过外部容器完成的，⽽Spring提供了这么⼀个容器，叫做：**IOC容器**.
⽆论是创建对象、处理对象之间的依赖关系、对象创建的时间还是对象的数量，都是在Spring的IOC容器上配置对象的信息。

**IOC的思想核⼼**在于，资源不由使⽤资源的双⽅管理，⽽由不使⽤资源的第三⽅管理，这可以带来很多好处。

+ 第⼀，资源集中管理，实现资源的可配置和易管理。
+ 第⼆，降低了使⽤资源双⽅的依赖程度，也就是耦合度。

### 1.持久层和业务层准备

#### 持久层Dao

1. AccountDao

   ```java
   /**
    * 账户的持久层接口
    */
   public interface AccountDao {
   
       /**
        * 模拟保存账户
        */
       void saveAccount();
   }
   ```

2. AccountDaoImpl

   ```java
   /**
    * 账户的持久层实现类
    */
   public class AccountDaoImpl implements AccountDao {
   
       public  void saveAccount(){
   
           System.out.println("保存了账户");
       }
   }
   ```

#### 业务层Service

1. AccountService

   ```java
   /**
    * 账户业务层的接口
    */
   public interface AccountService {
   
       /**
        * 模拟保存账户
        */
       void saveAccount();
   }
   ```

2. AccountServiceImpl

   ```java
   /**
    * 账户的业务层实现类
    */
   public class AccountServiceImpl implements AccountService {
   
       private AccountDao accountDao = new AccountDaoImpl();
   
       public void saveAccount() {
           accountDao.saveAccount();
       }
   }
   ```



### 2.基于XML的IOC入门

#### 1.在pom文件中导入maven依赖

```xml
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-context</artifactId>
    <version>5.0.2.RELEASE</version>
</dependency>
```

#### 2.创建配置文件bean.xml，在官网中找到配置模版

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd">

<bean id="accountService" class="cn.sucrelt.service.impl.AccountServiceImpl">
    <!-- collaborators and configuration for this bean go here -->
</bean>

<bean id="accountDao" class="cn.sucrelt.dao.impl.AccountDaoImpl">
</bean>

</beans>
```

**bean标签**：用于配置让spring创建对象，并且存入ioc容器之中。

+ id属性：对象的唯一标识。 
+ class属性：指定要创建对象的全限定类名。

#### 3.使用spring管理

```java
/**
 * 模拟一个表现层，用于调用业务层
 */
public class Client {
    /**
     * 获取Spring的IOC核心容器，并根据id获取对象
     *
     * @param args
     */
    public static void main(String[] args) {
        //1.获取核心容器对象
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("bean.xml");

        //2.根据id获取Bean对象
        AccountService accountService = (AccountService) applicationContext.getBean("accountService");
        AccountDao accountDao = applicationContext.getBean("accountDao", AccountDao.class);

        System.out.println(accountService);
        System.out.println(accountDao);
    }
}
```



### 3.IOC相关类的细节

1. ApplicationContext的三个常用实现类。

   基于xml配置：

   `ClassPathXmlApplicationContext`：加载类路径下的配置文件，推荐使用！

   `FileSystemXmlApplicationContext`：加载磁盘任意路径下的配置文件，必须有访问权限。

   基于注解配置：

   `AnnotationConfigApplicationContext`：读取注解创建容器。

2. BeanFactory和ApplicationContext 

   + BeanFactory是Spring 容器中的顶层接口，ApplicationContext 是它的子接口。
   + BeanFactory 和ApplicationContext 的创建对象的时间点不一样。
     ApplicationContext：直接加载，只要一读取配置文件，默认情况下就会创建对象。适用于单例对象。推荐使用。
     BeanFactory：延迟加载，根据id获取对象时创建对象。适用于多例对象。



### 4.Bean标签

#### 1.实例化bean的方式

+ 使用**默认构造函数**创建：在配置文件中使用bean标签并使用id和class属性后且没有其他属性和标签时，使用的就是该方式。如果类中没有默认构造函数，bean对象将创建失败。

  ```xml
  <!--spring配置文件-->
  <bean id="accountService" class="cn.sucrelt.service.impl.AccountServiceImpl"></bean>
  ```

  ```java
  //bean实体类中需要有默认构造函数
  public class AccountServiceImpl implements AccountService {
  
      public AccountServiceImpl(){
          System.out.println("service对象创建了。。。");
      }
  }
  ```

+ 使用**工厂类中的静态方法**创建对象，即使用某个类中的静态方法创建对象并存入spring容器中。

  ```xml
  <!--spring配置文件-->
  <bean id="accountService" class="cn.sucrelt.factory.StaticFactory" factory-method="getAccountService"></bean>
  ```

  ```java
  //存在一个对应的工厂类，其中包含生成该对象的静态方法
  public class StaticFactory {
      public static AccountService getAccountService() {
          return new AccountServiceImpl();
      }
  }
  ```

  配置文件中标签：

  + id属性：指定bean的id
  + class属性：指定静态工厂的全限定类名
  + factory-method属性：指定生产对象的静态方法

+ 使用**普通工厂类中的方法**创建对象并存入spring容器中。

  ```java
  <!--spring配置文件-->
  <bean id="instancFactory" class="cn.sucrelt.factory.InstanceFactory"></bean>
  <bean id="accountService" factory-bean="instancFactory" factory-method="getAccountService"></bean>
  ```

  ```java
  //存在一个对应的工厂类，其中包含生成该对象的方法
  public class InstanceFactory {
      public AccountService getAccountService() {
          return new AccountServiceImpl();
      }
  }
  ```

  配置文件中标签：

  + factory-bean属性：用于指定实例工厂bean的id
  + factory-method属性：用于指定实例工厂中创建对象的方法

+ **注意**：在一些情况中，我们需要使用第三方的jar包中的类作为bean对象，或者使用某个方法的返回值对应的类作为bean对象，后两种通过工厂中的方法实例化bean对象的方式提供了这种操作。

#### 2.bean标签的作用范围与生命周期

1. scope：指定对象的作用范围。

   + singleton ：默认值，单例的。
   + prototype ：多例的。
   + request ：WEB项目中，Spring创建一个Bean的对象，将对象存入到request域中。
   + session ：WEB项目中，Spring创建一个Bean的对象，将对象存入到session域中。
   + global-session ：WEB项目中，应用在Portlet环境（集群环境的会话范围）。如果没有Portlet环境那么globalSession相当于session。

2. 生命周期

   + 单例对象：scope="singleton"一个应用只有一个对象的实例。它的作用范围就是整个引用。
     +  对象出生：当应用加载，创建容器时，对象就被创建了。 
     + 对象活着：只要容器在，对象一直活着。 
     + 对象死亡：当应用卸载，销毁容器时，对象就被销毁了。 
   
   + 多例对象：scope="prototype" 每次访问对象时，都会重新创建对象实例。 
     + 对象出生：当使用对象时，创建新的对象实例。 
     + 对象活着：只要对象在使用中，就一直活着。 
     + 对象死亡：当对象长时间不用时，被**java的垃圾回收器**回收了。



### 5.依赖注入：Dependency Injection

**经常变化的数据，并不适用于配置文件注入的方式**

#### 1.注入的数据类型

1. 基本类型和String
2. 其他bean类型（在配置文件中或者注解配置过的bean）
3. 复杂类型/集合类型

#### 2.注入方式

1. 构造函数注入

   类中编写用于初始化类属性的构造函数

   ```java
   public class AccountServiceImpl implements AccountService {
   
       private String name;
       private Integer age;
       private Date birthday;
   
       public AccountServiceImpl(String name, Integer age, Date birthday) {
           this.name = name;
           this.age = age;
           this.birthday = birthday;
       }
       ...
   }
   ```

   在配置文件的bean标签内部使用`constructor-arg`标签

   ```xml
   <bean id="accountService" class="cn.sucrelt.service.impl.AccountServiceImpl">
       <constructor-arg name="name" value="test"></constructor-arg>
       <constructor-arg name="age" value="18"></constructor-arg>
       <constructor-arg name="birthday" ref="nowadays"></constructor-arg>
   </bean>
   
   <bean id="nowadays" class="java.util.Date"></bean>
   ```

   constructor-arg标签的属性：

   + type：指定参数在构造函数中的数据类型
   + index：指定参数在构造函数参数列表的索引位置
   + name：指定参数在构造函数中的名称，**常用**
   + value：给属性赋值，基本数据类型和String
   + ref：其他bean类型的值，在配置文件中需要定义了该bean，在spring核心容器中出现该对象，如例中nowadays

   优势：在获取bean对象时注入数据时必须的操作，适合采用此方式；

   弊端：改变了bean对象的实例化方式，创建对象时必须提供所有的数据。

2. set方法注入

   类中编写属性对应的setter方法

   ```java
   public class AccountServiceImpl2 implements AccountService {
   
       private String name;
       private Integer age;
       private Date birthday;
   
       public void setName(String name) {
           this.name = name;
       }
   
       public void setAge(Integer age) {
           this.age = age;
       }
   
       public void setBirthday(Date birthday) {
           this.birthday = birthday;
       }
   ```

   在配置文件的bean标签内部使用`property`标签

   ```xml
   <bean id="nowadays" class="java.util.Date"></bean>
   
   <bean id="accountService2" class="cn.sucrelt.service.impl.AccountServiceImpl2">
       <property name="name" value="test"></property>
       <property name="age" value="21"></property>
       <property name="birthday" ref="nowadays"></property>
   </bean>
   ```

   property标签的属性：

   + name：类中set方法后面的部分
   + value：给属性赋值，基本数据类型和String
   + ref：其他bean类型的值

   优势：创建对象时没有明确的限制，可以直接使用默认构造函数；

   弊端：某个成员必须有值，则获取对象时可能set方法没有执行。

3. 注解注入

4. 复杂类型（集合类型）的注入

   使用set注入方式，在配置文件的bean标签下使用property标签，在property标签中选择对应的子标签

   + List结构的标签：array，list，set 

   + Map结构的标签：map，props

   同一组中的标签可以互换使用

   ```xml
   <bean id="accountService3" class="cn.sucrelt.service.impl.AccountServiceImpl3">
       <!-- 给数组注入数据 -->
       <property name="myStrs">
           <set>
               <value>AAA</value>
               <value>BBB</value>
               <value>CCC</value>
           </set>
       </property> 
       <!-- 注入list集合数据 -->
       <property name="myList">
           <array>
               <value>AAA</value>
               <value>BBB</value>
               <value>CCC</value>
           </array>
       </property> 
   </bean>
   ```



## 基于注解的Spring-IOC

### 1.常用注解

#### 1.用于创建对象

+ 作用类似于XML文件中bean标签实现的功能

+ @Component

  ```java
  @Component("accountService")
  public class AccountServiceImpl implements AccountService {
  ...
  }
  ```

  相当于

  ```xml
  <bean id="accountService" class="cn.sucrelt.service.impl.AccountServiceImpl"></bean>
  ```

  Component注解用于告诉Spring创建该类对应的bean对象到Spring容器中，spring容器中以map结构存储对象，即key-value，存储bean对象时value为该类的类名，key值默认为类名首字母小写的字符串，在Component注解中可指定key值。

+ @Controller：一般用于表现层的注解。 

+ @Service：一般用于业务层的注解。 

+ @Repository：一般用于持久层的注解。

+ 以上三个注解都是针对Component的衍生注解，他们的作用及属性都是一样， 只不过是提供了更加明确的语义化。

  上面的Component注解可以使用以下Service注解代替

  ```java
  @Service("accountService")
  public class AccountServiceImpl implements AccountService {
  }
  ```

#### 2.用于注入数据

+ 作用类似于XML文件中bean标签中的property标签的功能

+ **注意**：当使用注解注入属性时，set方法可以省略；集合数据的注入只能使用xml配置。

+ @Autowired，可以注入类成员和方法的参数

  ```java
  public class AccountServiceImpl implements AccountService {
  
      @Autowired
      private AccountDao accountDao;
  
      public void saveAccount() {
          accountDao.saveAccount();
      }
  }
  ```

  相当于

  ```xml
  <bean id="accountDao" class="cn.sucrelt.service.impl.AccountDaoImlp"></bean>
  
  <bean id="accountService" class="cn.sucrelt.service.impl.AccountServiceImpl">
      <property name="accountDao" ref="accountDao"></property>
  </bean>
  ```

  Autowired注解只能注入bean类型，自动按照类型注入。首先在Spring容器中按照数据类型（AccountDao）查找对应的bean对象并进行注入，当有多个类型匹配时，使用要注入的对象变量名称（accountDao）作为bean的id，在Spring容器查找，找到了也可以注入成功，找不到就报错。

+ @Qualifier(“bean的id”)

  在自动按照类型注入的基础之上，再按照名称注入。它在给类成员注入时不能独立使用，必须和@Autowire一起使用；但是给方法参数注入时，可以独立使用。 

+ @Resource(name=“bean的id”)，直接按照Bean的id注入。

+ @Value(value="")，注入基本数据类型和String类型数据的

#### 3.用于改变作用范围

+ 作用类似于bean标签中的scope属性的功能

+ @Scope(value="")

  相当于

  ```xml
  <bean id="" class="" scope=""></bean>
  ```

  value：指定范围的值。 取值：singleton prototype request session globalsession

#### 4.与生命周期相关

+ 作用类似于bean标签中的init-method和destroy-method属性的功能

  相当于

  ```xml
  <bean id="" class="" init-method="" destroy-method="" /></bean>
  ```

  

+ @PostConstruct：用于指定初始化方法。

+ @PreDestroy：用于指定销毁方法。

### 2.新注解

使用注解开发后，虽然可以将项目中的类以注解的方式创建到spring容器中，而不适用bean标签，但是第三方库的类，依旧需要配置bean.xml以及使用bean创建。此时bean.xml文件如下：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/context
        https://www.springframework.org/schema/context/spring-context.xsd">

    <!--使用注解开发-->
    <context:component-scan base-package="cn.sucrelt"></context:component-scan>
    <!-- 配置QueryRunner-->
    <bean id="runner" class="org.apache.commons.dbutils.QueryRunner" scope="prototype">
        <!-- 注入数据源 -->
        <constructor-arg name="ds" ref="dataSoucre"></constructor-arg>
    </bean>
    <bean id="dataSoucre" class="com.mchange.v2.c3p0.ComboPooledDataSource">
        <property name="driverClass" value="com.mysql.jdbc.Driver"></property>
        <property name="jdbcUrl"
                  value="jdbc:mysql://localhost:3306/springioc?characterEncoding=UTF-8&amp;serverTimezone=GMT"></property>
        <property name="user" value="root"></property>
        <property name="password" value="123456"></property>
    </bean>

</beans>
```

使用以下新注解消除该bean.xml文件。

#### 1.@Configuration

用于指定当前类是一个spring配置类，当创建容器时会从该类上加载注解，该类即相当于bean.xml文件。

```java
@Configuration
public class SpringConfiguration {
    ...
}
```

**相应地**，获取容器时需要使用`AnnotationApplicationContext(有@Configuration注解的类.class)`。其实，当配置类作为该`AnnotationApplicationContext`方法的参数时，该注解可以省略不写。

```java
ApplicationContext applicationContext = new AnnotationConfigApplicationContext(SpringConfiguration.class);
AccountService accountService = applicationContext.getBean("accountService", AccountService.class);
```

#### 2.@ComponentScan

放在配置类上，用于指定spring在创建容器时要扫描的包，其中参数为默认的value或者basePackages，值为创建容器时要扫描的包名。

```java
@Configuration
@ComponentScan(basePackages = "cn.sucrelt")
public class SpringConfiguration {
}
```

相当于bean.xml中的

```xml
<context:component-scan base-package="cn.sucrelt"></context:component-scan>
```

#### 3.@Bean

该注解只能写在方法上，表明使用此方法创建一个对象，并且放入spring容器。其中name属性用于指定bean的id。当不写时，默认值是当前方法的名称。当使用该注解配置方法时，如果方法有参数，spring框架会去容器中查找有没有可用的bean对象，查找的方式和Autowired注解的作用是一样的。

```java
@Bean(name = "runner")
@Scope("prototype")
public QueryRunner createQueryRunner(DataSource dateSource) {
    return new QueryRunner(dateSource);
}

@Bean(name = "dataSource")
public DataSource createDataSource() {
    try {
        ComboPooledDataSource ds = new ComboPooledDataSource();
        ds.setDriverClass("com.mysql.jdbc.Driver");
        ds.setJdbcUrl("jdbc:mysql://localhost:3306/springioc?characterEncoding=UTF-8&amp;serverTimezone=GMT");
        ds.setUser("root");
        ds.setPassword("123456");
        return ds;
    } catch (Exception e) {
        throw new RuntimeException(e);
    }
}
```

相当于bean.xml中的

```xml
<!-- 配置QueryRunner-->
<bean id="runner" class="org.apache.commons.dbutils.QueryRunner" scope="prototype">
    <!-- 注入数据源 -->
    <constructor-arg name="ds" ref="dataSoucre"></constructor-arg>
</bean>
<bean id="dataSoucre" class="com.mchange.v2.c3p0.ComboPooledDataSource">
    <property name="driverClass" value="com.mysql.jdbc.Driver"></property>
    <property name="jdbcUrl"
              value="jdbc:mysql://localhost:3306/springioc?characterEncoding=UTF-8&amp;serverTimezone=GMT"></property>
    <property name="user" value="root"></property>
    <property name="password" value="123456"></property>
</bean>
```

#### 4.@Import--多个配置类

可以将上述的配置类拆分成一个数据库配置类JdbcConfig，SpringConfiguration作为一个总的配置类。

总配置类：

```java
@Configuration
@ComponentScan(basePackages = {"cn.sucrelt"})
public class SpringConfiguration {
}
```

数据库配置类：

```java
@Configuration
public class JdbcConfig {
    /**
     * 创建QueryRunner对象
     *
     * @param dateSource
     * @return
     */
    @Bean(name = "runner")
    @Scope("prototype")
    public QueryRunner createQueryRunner(DataSource dateSource) {
        return new QueryRunner(dateSource);
    }

    @Bean(name = "dataSource")
    public DataSource createDataSource() {
        try {
            ComboPooledDataSource ds = new ComboPooledDataSource();
            ds.setDriverClass("com.mysql.jdbc.Driver");
            ds.setJdbcUrl("jdbc:mysql://localhost:3306/springioc?characterEncoding=UTF-8&amp;serverTimezone=GMT");
            ds.setUser("root");
            ds.setPassword("123456");
            return ds;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
```

然而，此时执行将会报错，原因在于JdbcConfig这个类并没有被扫描，其中的相关方法并没有创建到spring容器中。有如下解决方法：

1. 在总配置类的ComponentScan注解中增加配置类的扫描包

   ```java
   @Configuration
   @ComponentScan(basePackages = {"cn.sucrelt","config"})
   public class SpringConfiguration {
   }
   ```

2. 在使用`AnnotationConfigApplicationContext`获取容器时其中参数加上`JdbcConfig.class`。

   ```java
   ApplicationContext applicationContext = new AnnotationConfigApplicationContext(SpringConfiguration.class,
           JdbcConfig.class);
   ```

   该方法不推荐，因为SpringConfiguration和JdbcConfig应该是上下级的关系，这种方式呈现的是并列的关系。

3. 使用**@Import注解**

   ```java
   @Configuration
   @ComponentScan(basePackages = "cn.sucrelt")
   @Import(JdbcConfig.class)
   public class SpringConfiguration {
   }
   ```

   该注解用于引入其他配置类，即将小的配置类全部加载到主配置类中，可以实现分配置文件配置的方法。

#### 5.@PropertySource

用于加载.properties文件中的配置。如配置数据源时，可以把连接数据库的信息写到properties配置文件中，就可以使用此注解指定properties配置文件的位置。

在`JdbcConfig`类中，定义成员变量获取Jdbc相关信息，并使用`@Value`进行注解，在该类上方使用`@PropertySource`注解指定获取Jdbc配置信息的文件位置，如果是在类路径下，需要写上classpath。

```java
@Configuration
@PropertySource("classpath:jdbcConfig.properties")
public class JdbcConfig {
    @Value("${jdbc.driver}")
    private String driver;
    @Value("${jdbc.url}")
    private String url;
    @Value("${jdbc.username}")
    private String username;
    @Value("${jdbc.password}")
    private String password;
    
    @Bean(name = "runner")
    @Scope("prototype")
    public QueryRunner createQueryRunner(DataSource dateSource) {
        return new QueryRunner(dateSource);
    }

    @Bean(name = "dataSource")
    public DataSource createDataSource() {
        try {
            ComboPooledDataSource ds = new ComboPooledDataSource();
            ds.setDriverClass(driver);
            ds.setJdbcUrl(url);
            ds.setUser(username);
            ds.setPassword(password);
            return ds;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
```



## Spring整合Junit

在测试类中，每个测试方法都有以下两行代码，在编写测试时都需要手动创建容器：

```java
ApplicationContext applicationContext = new AnnotationConfigApplicationContext(SpringConfiguration.class);
AccountService accountService = applicationContext.getBean("accountService", AccountService.class);
```

将Junit整合到spring中，在编写测试类时程序自动帮我们创建容器。无须手动创建。

### 1.导入依赖

在pom文件中引入依赖spring-test，用于整合spring和Junit

```xml
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-test</artifactId>
    <version>5.0.2.RELEASE</version>
</dependency>
```

### 2.@RunWith

在测试类上方使用`@RunWith`注解替换原有运行器，其中参数为`SpringJUnit4ClassRunner.class`。

### 3.@ContextConfiguration

使用`@ContextConfiguration`指定spring配置文件的位置，其中属性：

+ locations属性：用于指定配置文件的位置。如果是类路径下，需要用`classpath:`。

+ classes属性：用于指定注解的类。当不使用xml配置时，需要用此属性指定注解类的位置。

### 4.@Autowired

使用`@Autowired`给测试类中的变量注入数据

```java
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringConfiguration.class)
public class AccountServiceTest {

    @Autowired
    private AccountService accountService;
    
    @Test
    public void testFindAllAccount() {
        List<Account> accounts = accountService.findAllAccount();
        for (Account account :
                accounts) {
            System.out.println(account.toString());
        }
    }   
}
```



