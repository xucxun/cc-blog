package com.example.blog.controller;

import com.example.blog.common.Constant;
import com.example.blog.common.EventProducer;
import com.example.blog.entity.Article;
import com.example.blog.entity.Comment;
import com.example.blog.entity.Event;
import com.example.blog.service.ArticleService;
import com.example.blog.service.CommentService;
import com.example.blog.util.LoginUser;
import com.example.blog.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/comment")
public class CommentController{

    @Autowired
    private ArticleService articleService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private EventProducer eventProducer;

    @Autowired
    private LoginUser loginUser;

    @Autowired
    private RedisTemplate redisTemplate;

    @PostMapping("/save/{articleId}")
    public String save(@PathVariable("articleId") int articleId, Comment comment){
        commentService.save(comment);

        //评论后触发发布博客事件，更新搜索结果的评论数量
        if (comment.getEntityType() == Constant.ENTITY_TYPE_ARTICLE){
            // 触发发博客事件
           Event event = new Event()
                    .setTopic(Constant.TOPIC_PUBLISH)
                    .setUserId(comment.getUserId())
                    .setEntityType(Constant.ENTITY_TYPE_ARTICLE)
                    .setEntityId(articleId);
            eventProducer.emitEvent(event);

            // 评论后更新博客分数
            String redisKey = RedisKeyUtil.getArticleScoreKey();
            redisTemplate.opsForSet().add(redisKey, articleId);
        }

        String title = articleService.getById(articleId).getTitle();

        //评论后触发评论事件,供消费者消费
        Event event = new Event()
                .setTopic(Constant.TOPIC_COMMENT)
                .setUserId(loginUser.getUser().getId())
                .setEntityType(comment.getEntityType())
                .setEntityId(comment.getEntityId())
                .setData("articleId", articleId)
                .setData("title",title);

        //判断评论的是文章还是评论,
        if (comment.getEntityType() == Constant.ENTITY_TYPE_ARTICLE) {
            Article target = articleService.getById(comment.getEntityId());
            //用户评论自己的文章不发布通知
            if(target.getUserId() == loginUser.getUser().getId()){
                return "redirect:/article/" + articleId;
            }
            event.setEntityUserId(target.getUserId());
            event.setData("reply",comment.getContent());
        } else if (comment.getEntityType() == Constant.ENTITY_TYPE_COMMENT) {
            Comment target = commentService.findCommentById(comment.getEntityId());
            event.setEntityUserId(target.getUserId());
            if(target.getUserId() == loginUser.getUser().getId()){
                return "redirect:/article/" + articleId;
            }
            //获取被评论内容和回复内容
            event.setData("comment",target.getContent());
            event.setData("reply",comment.getContent());
        }
        //调用生产者发布评论通知
        eventProducer.emitEvent(event);

        return "redirect:/article/" + articleId;
    }
}
