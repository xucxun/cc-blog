package com.example.blog.service;

import com.example.blog.entity.Message;

import java.util.List;
import java.util.Map;

public interface MessageService {

    /**
     * 查询用户的会话数量
     */
    int countAllConversation(int userId);

    /**
     * 查询某个会话所包含的私信数量.
     */
    int countMessage(String conversationId);

    /**
     * 查询未读私信的数量
     */
    int countMessageUnread(int userId, String conversationId);

    /**
     * 修改消息状态为已读
     */
    int markRead(List<Integer> ids);

    /**
     * 发送私信
     */
    String send(String receiverName,String content);

    /**
     * 私信聊天列表
     */
    List<Map<String, Object>> listConversations(int offset, int limit);

    /**
     * 私信详情
     */
    List<Message> listMessage(String conversationId,int offset, int limit);

    /**
     * 私信详情
     */
    List<Map<String, Object>> listMessages(String conversationId,int offset, int limit);

}
