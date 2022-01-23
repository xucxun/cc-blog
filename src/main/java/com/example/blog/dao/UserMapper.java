package com.example.blog.dao;

import com.example.blog.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    User selectById(int id);

    User selectByAccount(String account);

    User selectByEmail(String email);

    int insertUser(User user);

    int updateStatus(int id, int status);

    int updateAvatar(int id, String avatar);

    int updatePassword(int id, String password);
}
