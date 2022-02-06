package com.example.blog.controller;

import com.example.blog.entity.Article;
import com.example.blog.entity.Comment;
import com.example.blog.entity.Page;
import com.example.blog.entity.User;
import com.example.blog.service.ArticleService;
import com.example.blog.service.CommentService;
import com.example.blog.service.UserService;
import com.example.blog.util.BlogConstant;
import com.example.blog.util.BlogUtil;
import com.example.blog.util.HostHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/article")
public class ArticleController implements BlogConstant {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @PostMapping("/save")
    @ResponseBody
    public String save(String title, String content){
        User user = hostHolder.getUser();
        if (user == null) {
            return BlogUtil.getJsonResult(403, "您还没有登录哦!");
        }
        if(StringUtils.isBlank(title) || StringUtils.isBlank(content)){
            return BlogUtil.getJsonResult(1, "标题或内容不能为空!");
        }

        Article article = new Article();
        article.setUserId(user.getId());
        article.setTitle(title);
        article.setContent(content);
        article.setCreateTime(new Date());

        articleService.save(article);

       return BlogUtil.getJsonResult(0, "文章发布成功!");

    }

    @GetMapping("/{id}")
    public String articleDetail(@PathVariable("id") int id, Model model, Page page) {
        Article article = articleService.getById(id);
        model.addAttribute("article",article);
        User user = userService.findUserById(article.getUserId());
        model.addAttribute("user",user);

        // 评论分页
        page.setLimit(5);
        page.setPath("/article/" + id);
        page.setRows(article.getCommentCount());

        List<Comment> commentList = commentService.listByEntity(ENTITY_TYPE_ARTICLE, article.getId(), page.getOffset(), page.getLimit());

        List<Map<String, Object>> commentVOList = new ArrayList<>();
        if (commentList != null) {
            for (Comment comment : commentList) {
                // 评论VO
                Map<String, Object> commentVO = new HashMap<>();
                // 评论
                commentVO.put("comment", comment);
                // 作者
                commentVO.put("user", userService.findUserById(comment.getUserId()));
                // 回复列表
                List<Comment> replyList = commentService.listByEntity(ENTITY_TYPE_COMMENT, comment.getId(), 0, Integer.MAX_VALUE);
                // 回复VO列表
                List<Map<String, Object>> replyVoList = new ArrayList<>();
                if (replyList != null) {
                    for (Comment reply : replyList) {
                        Map<String, Object> replyVo = new HashMap<>();
                        // 回复
                        replyVo.put("reply", reply);
                        // 作者
                        replyVo.put("user", userService.findUserById(reply.getUserId()));
                        // 回复目标
                        User target = reply.getTargetId() == 0 ? null : userService.findUserById(reply.getTargetId());
                        replyVo.put("target", target);
                        replyVoList.add(replyVo);
                    }
                    commentVO.put("replys", replyVoList);

                    // 回复数量
                    int replyCount = commentService.countAll(ENTITY_TYPE_COMMENT, comment.getId());
                    commentVO.put("replyCount", replyCount);

                    commentVOList.add(commentVO);
                }
            }
        }
        model.addAttribute("comments", commentVOList);
        return "front/article-detail";

    }

}
