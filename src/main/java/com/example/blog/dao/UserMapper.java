package com.example.blog.dao;

import com.example.blog.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface UserMapper {

    int selectUserCount();

    User selectById(int id);

    User selectByAccount(String account);

    User selectByEmail(String email);

    User selectByNickName(String nickName);

    int insertUser(User user);

    int updateStatus(@Param("id")int id, @Param("status")Integer status,@Param("updateTime") Date updateTime);

    void updateRole(@Param("id")int id, @Param("role")Integer role,@Param("updateTime") Date updateTime);

    int updateAvatar(@Param("id")int id,@Param("avatar") String avatar);

    int updatePassword(@Param("id")int id, @Param("password")String password);

    List<User> findUsers(@Param("offset")int offset, @Param("limit")int limit);

    List<User> searchUserList(@Param("nickName") String nickName,@Param("email") String email,@Param("role")Integer role,
                              @Param("status")Integer status,
                              @Param("offset")int offset, @Param("limit")int limit);

    int searchUserCount(@Param("nickName") String nickName,@Param("email") String email,@Param("role")Integer role,
                        @Param("status")Integer status);


}
