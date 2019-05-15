# sftp-pool
基于Jsch和apache common-pool实现的sftp连接池

##Demo
Spring Boot 
1、独立的Spring容器应用程序，不需要war包
Spring boot 可以以jar包形式独立运行，运行一个Spring Boot项目只需要通过java -jar xx.jar来运行。

2、内嵌tomcat
Spring Boot可以选择内嵌Tomcat、jetty或者Undertow,这样我们无须以war包形式部署项目。

3、提供Starter简化Maven配置
spring提供了一系列的start pom来简化Maven的依赖加载，例如，当你使用了spring-boot-starter-web，会自动加入如图5-1所示的依赖包。

4、开箱即用，无代码生产和xml配置
SpringBoot会根据在类路径中的jar包，类、为jar包里面的类自动配置Bean，这样会极大地减少我们要使用的配置。当然，SpringBoot只考虑大多数的开发场景，
并不是所有的场景，若在实际开发中我们需要配置Bean，而SpringBoot没有提供支持，则可以自定义自动配置。

SpringBoot不是借助与代码生成来实现的，而是通过条件注解来实现的，这是Spring4.x提供的新特性。

5、准生产的应用监控
SpringBoot提供基于http ssh telnet对运行时的项目进行监控
