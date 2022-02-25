package com.example.blog.service;

import com.example.blog.entity.LoginTicket;
import com.example.blog.entity.User;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Map;

public interface UserService {

    /**
     * 根据用户id查询用户
     */
    User findUserById(int id);

    /**
     * 根据用户昵称查询用户
     */
    User findUserByNickName(String nickName);

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
     *  更新用户头像
     */
    int updateAvatar(int userId, String avatar);

    /**
     * 查询用户凭证
     */
    LoginTicket findLoginTicket(String ticket);

    /**
     * 根据用户id从缓存中取值
     */
    User getCache(int userId);

    /**
     * 取不到时初始化缓存数据
     */
    User initCache(int userId);

    /**
     * 清除用户缓存数据
     */
    void clearCache(int userId);

    /**
     * 查询用户权限
     */
    Collection<? extends GrantedAuthority> getAuthorities(int userId);


}
