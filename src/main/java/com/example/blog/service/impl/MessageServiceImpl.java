package com.example.blog.service.impl;

import com.example.blog.dao.MessageMapper;
import com.example.blog.dao.UserMapper;
import com.example.blog.entity.Message;
import com.example.blog.entity.User;
import com.example.blog.service.MessageService;
import com.example.blog.util.HostHolder;
import com.example.blog.util.ResultUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private HostHolder hostHolder;


    @Override
    public int countAllConversation(int userId) {
        return messageMapper.conversationCount(userId);
    }

    @Override
    public int countMessage(String conversationId) {
        return messageMapper.messageCount(conversationId);
    }

    @Override
    public int countMessageUnread(int userId, String conversationId) {
        return messageMapper.messageCountUnread(userId,conversationId);
    }

    /**
     * 修改消息状态为已读
     */
    @Override
    public int markRead(List<Integer> ids) {
        Date date = new Date();
        return messageMapper.updateStatus(ids,1,date);
    }

    /**
     * 发送私信
     */
    @Override
    public String send(String receiverName, String content) {
        User receiver = userMapper.selectByNickName(receiverName);
        if (Objects.isNull(receiver)) {
            return ResultUtil.getJsonResult(1, "目标用户不存在!");
        }
        if(StringUtils.isBlank(content)){
            throw new IllegalArgumentException("内容不能为空!");
        }
        Message message = new Message();
        message.setSenderId(hostHolder.getUser().getId());
        message.setReceiverId(receiver.getId());
        if(message.getSenderId() != message.getReceiverId()) {
            if (message.getSenderId() < message.getReceiverId()) {
                message.setType(message.getSenderId() + "_" + message.getReceiverId());
            } else {
                message.setType(message.getReceiverId() + "_" + message.getSenderId());
            }
        }else{
            return ResultUtil.getJsonResult(1, "无效操作!");
        }
        message.setContent(content);
        message.setCreateTime(new Date());
        messageMapper.insertMessage(message);
        return ResultUtil.getJsonResult('0');
    }

    /**
     * 私信聊天列表
     */
    @Override
    public List<Map<String, Object>> listConversations(int offset, int limit) {
        User user = hostHolder.getUser();
        List<Message> chatList = messageMapper.queryConversations(user.getId(),offset,limit);
        List<Map<String, Object>> conversations = new ArrayList<>();
        if (chatList != null) {
            List<String> conversationIds = new ArrayList<>();
            for (Message message : chatList) {
                Map<String, Object> map = new HashMap<>();
                //会话id集合
                conversationIds.add(message.getType());
                map.put("conversation", message);
                map.put("messageCount", messageMapper.messageCount(message.getType()));
                map.put("unreadCount", messageMapper.messageCountUnread(user.getId(), message.getType()));
                int targetId = user.getId() == message.getSenderId() ? message.getReceiverId() : message.getSenderId();
                map.put("target", userMapper.selectById(targetId));
                conversations.add(map);
            }
        }

        return conversations;
    }

    @Override
    public List<Message> listMessage(String conversationId, int offset, int limit) {
        return messageMapper.queryMessages(conversationId,offset,limit);
    }

    /**
     * 私信详情
     */
    @Override
    public List<Map<String, Object>> listMessages(String conversationId,int offset, int limit) {
        List<Message> messageList = messageMapper.queryMessages(conversationId, offset, limit);
        List<Map<String, Object>> messages = new ArrayList<>();
        //针对当前用户来说，自己发给别人的在右边，别人发给自己的在左边
        if (messageList != null) {
            for (Message message : messageList) {
                Map<String, Object> map = new HashMap<>();
                map.put("message", message);
                map.put("sender", userMapper.selectById(message.getSenderId()));
                map.put("receiver", userMapper.selectById(message.getReceiverId()));
                messages.add(map);
            }
        }

        List<Integer> ids = getMessageIds(messageList);
        if (!ids.isEmpty()) {
            markRead(ids);
        }

        return messages;
    }

    private List<Integer> getMessageIds(List<Message> letterList) {
        List<Integer> ids = new ArrayList<>();

        if (letterList != null) {
            for (Message message : letterList) {
                if (hostHolder.getUser().getId() == message.getReceiverId() && message.getStatus() == 0) {
                    ids.add(message.getId());
                }
            }
        }
        return ids;
    }


}
