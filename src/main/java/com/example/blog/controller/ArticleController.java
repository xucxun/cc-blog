package com.example.blog.controller;

import com.example.blog.entity.Article;
import com.example.blog.entity.Page;
import com.example.blog.entity.User;
import com.example.blog.service.ArticleService;
import com.example.blog.service.CommentService;
import com.example.blog.service.LikeService;
import com.example.blog.service.UserService;
import com.example.blog.common.Constant;
import com.example.blog.util.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/article")
public class ArticleController implements Constant {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private LoginUser loginUser;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private LikeService likeService;

    @PostMapping("/save")
    @ResponseBody
    public String save(String title, String content){
       return (articleService.add(title,content));

    }

    @GetMapping("/{id}")
    public String articleDetail(@PathVariable("id") int id, Model model, Page page) {
        //文章详情
        Article article = articleService.getById(id);
        User user = userService.findUserById(article.getUserId());
        //评论列表
        List<Map<String, Object>> commentVOList = commentService.listComments(id, page.getOffset() ,page.getLimit());
        //文章点赞数量
        Long likeCount = likeService.countLike(ENTITY_TYPE_ARTICLE,id);
        //点赞状态
        int likeStatus = loginUser.getUser() == null ? 0 : likeService.likeStatus(loginUser.getUser().getId(),ENTITY_TYPE_ARTICLE,id);
        // 评论分页
        page.setLimit(5);
        page.setPath("/article/" + id);
        page.setRows(article.getCommentCount());

        model.addAttribute("article",article);
        model.addAttribute("user",user);
        model.addAttribute("comments", commentVOList);
        model.addAttribute("likeCount",likeCount);
        model.addAttribute("likeStatus",likeStatus);
        return "front/article-detail";

    }

}
