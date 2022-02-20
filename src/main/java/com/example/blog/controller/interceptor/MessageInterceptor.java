package com.example.blog.controller.interceptor;


import com.example.blog.entity.User;
import com.example.blog.service.MessageService;
import com.example.blog.util.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class MessageInterceptor implements HandlerInterceptor {

    @Autowired
    private LoginUser loginUser;

    @Autowired
    private MessageService messageService;

    //在Controller之后执行，写完拦截器后要在WebMvcConfig中配置
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        User user = loginUser.getUser();
        if (user != null && modelAndView != null) {
            int messageUnreadCount = messageService.countMessageUnread(user.getId(), null);
            int noticeUnreadCount = messageService.countNoticeUnread(user.getId(), null);
            int commentUnreadCount = messageService.countNoticeUnread(user.getId(), "comment");
            int likeUnreadCount = messageService.countNoticeUnread(user.getId(), "like");
            modelAndView.addObject("noticeUnreadCount",noticeUnreadCount);
            modelAndView.addObject("messageUnreadCount",messageUnreadCount);
            modelAndView.addObject("commentUnreadCount",commentUnreadCount);
            modelAndView.addObject("likeUnreadCount",likeUnreadCount);

        }
    }
}
