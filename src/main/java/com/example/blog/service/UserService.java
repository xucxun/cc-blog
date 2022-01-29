package com.example.blog.service;

import com.example.blog.entity.LoginTicket;
import com.example.blog.entity.User;

import java.util.Map;

public interface UserService {

    /**
     * 根据用户id查询用户
     */
    User findUserById(int id);

    /**
     * 用户注册
     */
    Map<String, Object> register(User registerUser,String confirmPassword);

    /**
     * 用户激活
     */
    int activation(int userId, String code);

    /**
     * 用户登录
     */
    Map<String, Object> login(String username, String password, int expiredSeconds);

    /**
     * 用户退出
     */
    void logout(String ticket);
    /**
     *
     */
    int updateAvatar(int userId, String avatar);

    LoginTicket findLoginTicket(String ticket);
}
