package com.example.blog.controller;

import com.example.blog.entity.User;
import com.example.blog.service.LikeService;
import com.example.blog.util.BlogConstant;
import com.example.blog.util.HostHolder;
import com.example.blog.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
public class LikeController implements BlogConstant {

    @Autowired
    private LikeService likeService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private RedisTemplate redisTemplate;

    @PostMapping("/like")
    @ResponseBody
    public String like(int entityType, int entityId,int entityUserId) {
        User user = hostHolder.getUser();
        // 点赞
        likeService.like(user.getId(), entityType, entityId,entityUserId);
        // 数量
        long likeCount = likeService.countLike(entityType, entityId);
        // 状态
        int likeStatus = likeService.likeStatus(user.getId(), entityType, entityId);

        Map<String, Object> map = new HashMap<>();
        map.put("likeCount", likeCount);
        map.put("likeStatus", likeStatus);

        return ResultUtil.getJsonResult(0, null, map);
    }


}
