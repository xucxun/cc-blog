package com.example.blog;

import com.example.blog.util.MailClient;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
@Ignore
@ContextConfiguration(classes = BlogApplication.class)
public class MailTests {

    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;

    @Test
    public void testText() { mailClient.sendMail("xucxun@qq.com", "TEST", "测试邮件发送成功！");
    }

    @Test
    public void testHtmlMail() {
        Context context = new Context();
        context.setVariable("username", "sunday");

        String content = templateEngine.process("/mail/demo", context);
        System.out.println(content);

        mailClient.sendMail("lihonghe@nowcoder.com", "HTML", content);
    }

    @Test
    public static String generateUUID(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }

}
