package com.example.blog.service;

import com.example.blog.entity.Message;

import java.util.List;

public interface MessageService {

    /**
     * 查询用户会话列表，每条会话返回最新一条私信
     */
    List<Message> listConversations(int userId, int offset, int limit);

    /**
     * 查询用户的会话数量
     */
    int countAllConversation(int userId);

    /**
     * 查询某个会话所包含的私信列表.
     */
    List<Message> listMessages(String conversationId, int offset, int limit);

    /**
     * 查询某个会话所包含的私信数量.
     */
    int countMessage(String conversationId);

    /**
     * 查询未读私信的数量
     */
    int countMessageUnread(int userId, String conversationId);
}
