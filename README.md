#### 1、项目介绍

本项目完成了一个开源社区博客系统的实现，用 Spring Boot框架进行开发，使用MyBatis操作数据库，使用Spring Security实现用户权限认证；采用MySQL和Redis实现数据的存储，同时使用ElasticSearch实现文章的模糊搜索功能。

#### 2、技术栈

1.  开发框架：SpringBoot + Mybatis 
2.  Java开发环境：JDK 1.8
3.  Java开发工具：Intellij IDEA 2021
4.  服务器: Tomcat 8.0
5.  数据库: MySQL 5.6
6.  中间件：分布式缓存Redis + 全文检索ElasticSearch + Kafka

#### 3、项目部署

下载elasticsearch-6.4.3及对应的IK中文分词器, 打开elasticsearch的bin目录，打开elasticsearch.bat 

下载kafka_2.12-2.3.1，启动zookeeper，执行以下命令

G:\JavaTools\kafka_2.12-2.3.1\bin\windows\zookeeper-server-start.bat G:\JavaTools\kafka_2.12-2.3.1\config\zookeeper.properties

启动kafka ，执行以下命令

G:\JavaTools\kafka_2.12-2.3.1\bin\windows\kafka-server-start.bat G:\JavaTools\kafka_2.12-2.3.1\config\server.properties

#### 4. 注意事项

在  applicaition.properties中修改邮箱配置，输入自己邮箱对应smtps的username和password，也可换成其他类型的邮箱

```
# Mail配置
spring.mail.host=smtp.qq.com
spring.mail.port=465
spring.mail.username=xx@qq.com
spring.mail.password=xxxx
spring.mail.protocol=smtps
spring.mail.properties.mail.smtp.ssl.enable=true
```



