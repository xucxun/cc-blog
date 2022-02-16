package com.example.blog.controller;

import com.example.blog.entity.Message;
import com.example.blog.entity.Page;
import com.example.blog.entity.User;
import com.example.blog.service.MessageService;
import com.example.blog.service.UserService;
import com.example.blog.util.BlogConstant;
import com.example.blog.util.HostHolder;
import com.example.blog.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
//        Integer.valueOf("abc");
        User user = hostHolder.getUser();
        page.setLimit(5);
        page.setPath("/messages");
        page.setRows(messageService.countAllConversation(user.getId()));

        //会话列表
        List<Map<String, Object>> conversations = messageService.listConversations(page.getOffset(), page.getLimit());
        model.addAttribute("conversations", conversations);

        // 查询未读消息数量
        int messageUnreadCount = messageService.countMessageUnread(user.getId(), null);
        model.addAttribute("messageUnreadCount", messageUnreadCount);
//        return "/front/letter";
//        return "/front/messages";
          return "test/test_chat_v4";
    }

    //私信详细信息
    @GetMapping("/messages/{conversationId}")
    public String listMessageDetail(@PathVariable("conversationId") String conversationId, Page page, Model model) {
        page.setLimit(10);
        page.setPath("/messages/" + conversationId);
        page.setRows(messageService.countMessage(conversationId));

        List<Map<String, Object>> messages = messageService.listMessages(conversationId, page.getOffset(),
                page.getLimit());
        model.addAttribute("messages", messages);
        // 私信目标
        model.addAttribute("target", getMessageTarget(conversationId));

//       return "/front/letter-detail";
        return "test/chat_detail";
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

    @PostMapping("/messages/send")
    @ResponseBody
    public String sendMessage(String receiverName, String content){
         return(messageService.send(receiverName,content));
    }
}
