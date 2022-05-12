package com.example.blog.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.blog.entity.Message;
import com.example.blog.entity.Page;
import com.example.blog.entity.User;
import com.example.blog.service.MessageService;
import com.example.blog.service.UserService;
import com.example.blog.common.Constant;
import com.example.blog.util.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class MessageController{

    @Autowired
    private MessageService messageService;

    @Autowired
    private LoginUser loginUser;

    @Autowired
    private UserService userService;

//    @GetMapping("/messages")
//    public String listMessage(Model model, Page page){
//        User user = loginUser.getUser();
//        page.setLimit(5);
//        page.setPath("/messages");
//        page.setRows(messageService.countAllConversation(user.getId()));
//
//        //会话列表
//        List<Map<String, Object>> conversations = messageService.listConversations(page.getOffset(), page.getLimit());
//        model.addAttribute("conversations", conversations);
//
//        // 查询未读消息数量
//        int messageUnreadCount = messageService.countMessageUnread(user.getId(), null);
//        model.addAttribute("messageUnreadCount", messageUnreadCount);
////        return "/front/letter";
////        return "/front/messages";
//          return "test/test_chat_v4";
//    }
//
//    //私信详细信息
//    @GetMapping("/messages/{conversationId}")
//    public String listMessageDetail(@PathVariable("conversationId") String conversationId, Page page, Model model) {
//        page.setLimit(10);
//        page.setPath("/messages/" + conversationId);
//        page.setRows(messageService.countMessage(conversationId));
//
//        List<Map<String, Object>> messages = messageService.listMessages(conversationId, page.getOffset(),
//                page.getLimit());
//        model.addAttribute("messages", messages);
//        // 私信目标
//        model.addAttribute("target", getMessageTarget(conversationId));
//
////       return "/front/letter-detail";
//        return "test/chat_detail";
//    }
//
//    private User getMessageTarget(String conversationId) {
//        String[] ids = conversationId.split("_");
//        int id0 = Integer.parseInt(ids[0]);
//        int id1 = Integer.parseInt(ids[1]);
//
//        if (loginUser.getUser().getId() == id0) {
//            return userService.findUserById(id1);
//        } else {
//            return userService.findUserById(id0);
//        }
//    }
//
//    @PostMapping("/messages/send")
//    @ResponseBody
//    public String sendMessage(String receiverName, String content){
//         return(messageService.send(receiverName,content));
//    }

    @GetMapping("/notice/{topic}")
    public String listNotices(@PathVariable("topic") String topic, Page page,Model model) {
        User user = loginUser.getUser();
        page.setLimit(5);
        page.setPath("/notice/" + topic);
        page.setRows(messageService.countNotice(user.getId(), topic));

        //所有类型通知未读数量
        int noticeUnreadCount = messageService.countNoticeUnread(user.getId(), null);
        model.addAttribute("noticeUnreadCount", noticeUnreadCount);

        int commentUnreadCount = messageService.countNoticeUnread(user.getId(), Constant.TOPIC_COMMENT);
        int likeUnreadCount = messageService.countNoticeUnread(user.getId(), Constant.TOPIC_LIKE);

        model.addAttribute("commentUnreadCount",commentUnreadCount);
        model.addAttribute("likeUnreadCount",likeUnreadCount);

        //当前类型通知列表
        List<Message> noticeList = messageService.listNotices(user.getId(), topic, page.getOffset(), page.getLimit());
        // 设置已读
        List<Integer> ids = getNoticeIds(noticeList);
        if (!ids.isEmpty()) {
            messageService.markRead(ids);
        }
        List<Map<String, Object>> noticeVOList = new ArrayList<>();
        if (noticeList != null) {
            for (Message notice : noticeList) {
                Map<String, Object> map = new HashMap<>();
                map.put("notice", notice);
                String content = HtmlUtils.htmlUnescape(notice.getContent());
                Map<String, Object> data = JSONObject.parseObject(content, HashMap.class);
                map.put("user", userService.findUserById((Integer) data.get("userId")));
                map.put("entityType", data.get("entityType"));
                map.put("entityId", data.get("entityId"));
                map.put("articleId", data.get("articleId"));
                //获取文章标题
                map.put("title",data.get("title"));
                //获取被评论内容
                map.put("comment",data.get("comment"));
                //获取回复
                map.put("reply",data.get("reply"));

                noticeVOList.add(map);
            }
            model.addAttribute("notices", noticeVOList);
        }

        return "/front/notice-detail";
    }

    //得到未读的id集合
    private List<Integer> getNoticeIds(List<Message> letterList) {
        List<Integer> ids = new ArrayList<>();

        if (letterList != null) {
            for (Message message : letterList) {
                if (loginUser.getUser().getId() == message.getReceiverId() && message.getStatus() == 0) {
                    ids.add(message.getId());
                }
            }
        }

        return ids;
    }
}
