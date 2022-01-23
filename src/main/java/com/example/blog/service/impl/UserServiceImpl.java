package com.example.blog.service.impl;

import com.example.blog.dao.UserMapper;
import com.example.blog.entity.User;
import com.example.blog.service.UserService;
import com.example.blog.util.BlogConstant;
import com.example.blog.util.BlogUtil;
import com.example.blog.util.MailClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class UserServiceImpl implements UserService, BlogConstant{

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${blog.path.domain}")
    private String domain;


    @Override
    public User findUserById(int id) {
        return userMapper.selectById(id);
    }

    @Override
    public Map<String, Object> register(User registerUser,String confirmPassword) {
        Map<String, Object> map = new HashMap<>();
        if (registerUser == null) {
            throw new IllegalArgumentException("参数不能为空！");
        }
        if (StringUtils.isBlank(registerUser.getAccount())) {
            map.put("accountMsg", "账号不能为空！");
            return map;
        }
        User registeredUser = userMapper.selectByAccount(registerUser.getAccount());
        if (registeredUser != null) {
            map.put("accountMsg", "该账号已存在！");
            return map;
        }
        if (StringUtils.isBlank(registerUser.getNickName())) {
            map.put("nameMsg", "昵称不能为空！");
            return map;
        }
        if (StringUtils.isBlank(registerUser.getPassword())) {
            map.put("passwordMsg", "密码不能为空！");
            return map;
        }
        if(!registerUser.getPassword().equals(confirmPassword)){
            map.put("confirmPasswordMsg","两次输入的密码不一致！");
            return map;
        }
        if (StringUtils.isBlank(registerUser.getEmail())) {
            map.put("emailMsg", "邮箱不能为空！");
            return map;
        }
        registeredUser = userMapper.selectByEmail(registerUser.getEmail());
        if (registeredUser != null) {
            map.put("emailMsg", "该邮箱已被注册！");
            return map;
        }
        // 注册用户
        registerUser.setSalt(BlogUtil.generateUUID().substring(0, 5));  //生成5位的盐
        registerUser.setPassword(BlogUtil.md5(registerUser.getPassword() + registerUser.getSalt()));
        registerUser.setRole(0);    //0-普通用户;
        registerUser.setStatus(0);  //0-未激活;
        registerUser.setActivationCode(BlogUtil.generateUUID());
        //获取牛客网随机图片作为用户默认头像
        registerUser.setAvatar(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        registerUser.setCreateTime(new Date());
        userMapper.insertUser(registerUser);

        // 激活邮件,使用thymeleaf创建的对象携带变量
        Context context = new Context();
        context.setVariable("email",registerUser.getEmail());
        //生成激活链接,用于激活用户状态
        String url = domain  + "/activation/" + registerUser.getId() + "/" +registerUser.getActivationCode();
        context.setVariable("url", url);
        //使用模板引擎，利用thymeleaf，将context放到/mail/activation.html文件中，然后再发送给邮箱
        String content = templateEngine.process("/mail/activation", context);
        mailClient.sendMail(registerUser.getEmail(), "激活账号", content);

        return map;
    }

    public int activation(int userId, String code) {
        User user = userMapper.selectById(userId);
        if (user.getStatus() == 1) {
            return ACTIVATION_REPEAT;
        } else if (user.getActivationCode().equals(code)) {
            userMapper.updateStatus(userId, 1);
            return ACTIVATION_SUCCESS;
        } else {
            return ACTIVATION_FAILURE;
        }
    }


}
