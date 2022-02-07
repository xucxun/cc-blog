package com.example.blog.dao;

import com.example.blog.entity.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MessageMapper {

    List<Message> queryConversations(@Param("userId")int userId, @Param("offset")int offset, @Param("limit")int limit);

    int conversationCount(@Param("userId")int userId);

    List<Message> queryMessages(@Param("conversationId")String conversationId, @Param("offset")int offset, @Param("limit") int limit);

    int messageCount(@Param("conversationId")String conversationId);

    int messageCountUnread(@Param("userId")int userId, @Param("conversationId")String conversationId);
}
