package com.example.blog.controller;

import com.example.blog.entity.Message;
import com.example.blog.entity.Page;
import com.example.blog.entity.User;
import com.example.blog.service.MessageService;
import com.example.blog.service.UserService;
import com.example.blog.util.BlogConstant;
import com.example.blog.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class MessageController implements BlogConstant {

    @Autowired
    private MessageService messageService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserService userService;

    @GetMapping("/messages")
    public String listMessage(Model model, Page page){
        User user = hostHolder.getUser();
        page.setLimit(5);
        page.setPath("/messages");
        page.setRows(messageService.countAllConversation(user.getId()));

        // 会话列表
        List<Message> conversationList = messageService.listConversations(user.getId(), page.getOffset(), page.getLimit());
        List<Map<String, Object>> conversations = new ArrayList<>();
        if (conversationList != null) {
            for (Message message : conversationList) {
                Map<String, Object> map = new HashMap<>();
                map.put("conversation", message);
                map.put("messageCount", messageService.countMessage(message.getType()));
                map.put("unreadCount", messageService.countMessageUnread(user.getId(), message.getType()));
                int targetId = user.getId() == message.getSenderId() ? message.getReceiverId() : message.getSenderId();
                map.put("target", userService.findUserById(targetId));

                conversations.add(map);
            }
        }
        model.addAttribute("conversations", conversations);

        // 查询未读消息数量
        int messageUnreadCount = messageService.countMessageUnread(user.getId(), null);
        model.addAttribute("messageUnreadCount", messageUnreadCount);
        return "/front/letter";
    }

    //私信详细信息
    @GetMapping("/messages/{conversationId}")
    public String listMessageDetail(@PathVariable("conversationId") String conversationId, Page page, Model model) {
        page.setLimit(10);
        page.setPath("/messages/" + conversationId);
        page.setRows(messageService.countMessage(conversationId));

        // 私信列表
        List<Message> letterList = messageService.listMessages(conversationId, page.getOffset(), page.getLimit());
        List<Map<String, Object>> messages = new ArrayList<>();
        if (letterList != null) {
            for (Message message : letterList) {
                Map<String, Object> map = new HashMap<>();
                map.put("message", message);
                map.put("sender", userService.findUserById(message.getSenderId()));
                messages.add(map);
            }
        }
        model.addAttribute("messages", messages);

        // 私信目标
        model.addAttribute("target", getMessageTarget(conversationId));


        return "/front/letter-detail";

    }

    private User getMessageTarget(String conversationId) {
        String[] ids = conversationId.split("_");
        int id0 = Integer.parseInt(ids[0]);
        int id1 = Integer.parseInt(ids[1]);

        if (hostHolder.getUser().getId() == id0) {
            return userService.findUserById(id1);
        } else {
            return userService.findUserById(id0);
        }
    }

    //得到未读的id集合
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
