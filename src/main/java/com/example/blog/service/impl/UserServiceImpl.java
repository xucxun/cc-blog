package com.example.blog.service.impl;

import com.example.blog.dao.LoginTicketMapper;
import com.example.blog.dao.UserMapper;
import com.example.blog.entity.LoginTicket;
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

    @Autowired
    private LoginTicketMapper loginTicketMapper;

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
        if (registerUser.getPassword().length() < 6) {
            map.put("passwordMsg", "密码至少需要6位！");
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
        registerUser.setActivationCode(BlogUtil.generateUUID());   //生成唯一验证码
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
        //使用模板引擎配置邮箱内容发送给邮箱
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

    @Override
    public Map<String, Object> login(String account, String password, int expiredSeconds) {
        Map<String, Object> map = new HashMap<>();

        if (StringUtils.isBlank(account)){
            map.put("accountMsg", "账号不能为空！");
            return map;
        }
        if (StringUtils.isBlank(password)) {
            map.put("passwordMsg", "密码不能为空！");
            return map;
        }

        User user = userMapper.selectByAccount(account);
        if (user == null) {
            map.put("accountMsg", "该账号不存在！");
            return map;
        }

        // 验证状态
        if (user.getStatus() == 0) {
            map.put("accountMsg", "该账号未激活！");
            return map;
        }

        if (user.getStatus() == 2) {
            map.put("accountMsg", "该账号已被拉黑！请联系管理员xucxun@qq.com！");
            return map;
        }
        // 验证密码
        password = BlogUtil.md5(password + user.getSalt());
        if (!user.getPassword().equals(password)) {
            map.put("passwordMsg", "密码不正确！");
            return map;
        }

        // 生成登录凭证，即cookie
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(user.getId());
        loginTicket.setTicket(BlogUtil.generateUUID());
        loginTicket.setStatus(0);  //0-有效
        //ms为单位，需要乘以1000L。需要转换为long型，否则会发生数据丢失
        loginTicket.setExpired(new Date(System.currentTimeMillis() + expiredSeconds * 1000L));
        loginTicketMapper.insertLoginTicket(loginTicket);

        map.put("ticket", loginTicket.getTicket());
        return map;
    }

    @Override
    public void logout(String ticket) {
        loginTicketMapper.updateStatus(ticket,1);
    }

    @Override
    public int updateAvatar(int userId, String avatar) {
        return userMapper.updateAvatar(userId, avatar);
    }

    public LoginTicket findLoginTicket(String ticket) {
        return loginTicketMapper.selectByTicket(ticket);

    }


}
