package com.example.blog.service;

import com.example.blog.entity.User;

public interface UserService {

    /**
     * 根据用户id查询用户
     */
    User findUserById(int id);
}
