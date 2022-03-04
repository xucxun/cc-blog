package com.example.blog.controller;

import com.example.blog.common.EventProducer;
import com.example.blog.entity.Comment;
import com.example.blog.entity.Event;
import com.example.blog.entity.User;
import com.example.blog.service.ArticleService;
import com.example.blog.service.CommentService;
import com.example.blog.service.LikeService;
import com.example.blog.common.Constant;
import com.example.blog.util.LoginUser;
import com.example.blog.util.RedisKeyUtil;
import com.example.blog.util.BlogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
public class LikeController{

    @Autowired
    private LikeService likeService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private LoginUser loginUser;

    @Autowired
    private EventProducer eventProducer;

    @Autowired
    private RedisTemplate redisTemplate;

    @PostMapping("/like")
    @ResponseBody
    public String like(int entityType, int entityId,int entityUserId,int articleId) {
        User user = loginUser.getUser();
        // 点赞
        likeService.like(user.getId(), entityType, entityId,entityUserId);
        // 数量
        long likeCount = likeService.countLike(entityType, entityId);
        // 状态
        int likeStatus = likeService.likeStatus(user.getId(), entityType, entityId);

        Map<String, Object> map = new HashMap<>();
        map.put("likeCount", likeCount);
        map.put("likeStatus", likeStatus);

        String title = articleService.getById(articleId).getTitle();

        // 触发点赞事件,用户点赞自己不处理
        if (likeStatus == 1 && loginUser.getUser().getId()!=entityUserId) {
            Event event = new Event()
                    .setTopic(Constant.TOPIC_LIKE)
                    .setUserId(loginUser.getUser().getId())
                    .setEntityType(entityType)
                    .setEntityId(entityId)
                    .setEntityUserId(entityUserId)
                    .setData("articleId",articleId)
                    .setData("title",title);
           if(entityType == Constant.ENTITY_TYPE_COMMENT){
               Comment target = commentService.findCommentById(entityId);
                event.setData("comment",target.getContent());
            }
            eventProducer.emitEvent(event);
        }

        if(entityType == Constant.ENTITY_TYPE_ARTICLE) {
            // 计算博客分数
            String redisKey = RedisKeyUtil.getArticleScoreKey();
            redisTemplate.opsForSet().add(redisKey, articleId);
        }

        return BlogUtil.getJsonResult(0, null, map);
    }


}
