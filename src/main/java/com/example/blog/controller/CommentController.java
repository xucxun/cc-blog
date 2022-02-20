package com.example.blog.controller;

import com.example.blog.common.Constant;
import com.example.blog.common.EventProducer;
import com.example.blog.entity.Article;
import com.example.blog.entity.Comment;
import com.example.blog.entity.Event;
import com.example.blog.service.ArticleService;
import com.example.blog.service.CommentService;
import com.example.blog.util.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/comment")
public class CommentController implements Constant {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private EventProducer eventProducer;

    @Autowired
    private LoginUser loginUser;

    @PostMapping("/save/{articleId}")
    public String save(@PathVariable("articleId") int articleId, Comment comment){
        commentService.save(comment);

        String title = articleService.getById(articleId).getTitle();

        //触发评论事件
        Event event = new Event()
                .setTopic(TOPIC_COMMENT)
                .setUserId(loginUser.getUser().getId())
                .setEntityType(comment.getEntityType())
                .setEntityId(comment.getEntityId())
                .setData("articleId", articleId)
                .setData("title",title);

        //判断评论的是文章还是评论,
        if (comment.getEntityType() == ENTITY_TYPE_ARTICLE) {
            Article target = articleService.getById(comment.getEntityId());
            //用户评论自己的文章不发布通知
            if(target.getUserId() == loginUser.getUser().getId()){
                return "redirect:/article/" + articleId;
            }
            event.setEntityUserId(target.getUserId());
            event.setData("reply",comment.getContent());
        } else if (comment.getEntityType() == ENTITY_TYPE_COMMENT) {
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
