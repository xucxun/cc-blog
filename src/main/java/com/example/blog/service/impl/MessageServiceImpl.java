package com.example.blog.service.impl;

import com.example.blog.dao.MessageMapper;
import com.example.blog.entity.Message;
import com.example.blog.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageMapper messageMapper;

    @Override
    public List<Message> listConversations(int userId, int offset, int limit) {
        return messageMapper.queryConversations(userId,offset,limit);
    }

    @Override
    public int countAllConversation(int userId) {
        return messageMapper.conversationCount(userId);
    }

    @Override
    public List<Message> listMessages(String conversationId, int offset, int limit) {
        return messageMapper.queryMessages(conversationId,offset,limit);
    }

    @Override
    public int countMessage(String conversationId) {
        return messageMapper.messageCount(conversationId);
    }

    @Override
    public int countMessageUnread(int userId, String conversationId) {
        return messageMapper.messageCountUnread(userId,conversationId);
    }
}
