package com.example.blog.controller;

import com.example.blog.common.EventProducer;
import com.example.blog.entity.Article;
import com.example.blog.entity.Event;
import com.example.blog.entity.Page;
import com.example.blog.entity.User;
import com.example.blog.service.ArticleService;
import com.example.blog.service.CommentService;
import com.example.blog.service.LikeService;
import com.example.blog.service.UserService;
import com.example.blog.common.Constant;
import com.example.blog.util.LoginUser;
import com.example.blog.util.ResultUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/article")
public class ArticleController{

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

    @Autowired
    private EventProducer eventProducer;

//    @PostMapping("/save")
//    @ResponseBody
//    public String save(String title, String content){
//        User user = loginUser.getUser();
//        if (user == null) {
//            return ResultUtil.getJsonResult(403, "您还没有登录哦!");
//        }
//        if(StringUtils.isBlank(title) || StringUtils.isBlank(content)){
//            return ResultUtil.getJsonResult(1, "标题或内容不能为空!");
//        }
//
//        Article article = new Article();
//        article.setUserId(user.getId());
//        article.setTitle(title);
//        article.setContent(content);
//        article.setCreateTime(new Date());
//        articleService.add(article);
//
//        // 触发发博客事件，将文章保存到es服务器里
//        Event event = new Event()
//                .setTopic(Constant.TOPIC_PUBLISH)
//                .setUserId(user.getId())
//                .setEntityType(Constant.ENTITY_TYPE_ARTICLE)
//                .setEntityId(article.getId());
//        eventProducer.emitEvent(event);
//       return ResultUtil.getJsonResult(0,"文章发布成功！");
//    }

    /**
     * 新增文章页面
     */
    @GetMapping("/input")
    public String toAddBlog(Model model){
        //返回一个blog对象给前端th:object
        model.addAttribute("article", new Article());
        return "/front/article-input";
    }

    /**
     * 编辑文章页面
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/input/{id}")
    public String toEditBlog(@PathVariable int id,Model model){
        Article article = articleService.getById(id);
        model.addAttribute("article", article);
        return "/front/article-input";
    }



    @PostMapping("/save")
    public String save(Article article){
        User user = loginUser.getUser();
        if (user == null) {
            return ResultUtil.getJsonResult(403, "您还没有登录哦!");
        }
        if(StringUtils.isBlank(article.getTitle()) || StringUtils.isBlank(article.getContent())){
            return ResultUtil.getJsonResult(1, "标题或内容不能为空!");
        }

        if(article.getId()==0){
            articleService.add(article);
        }else {
            articleService.update(article);
        }

        // 触发发博客事件，将文章保存到es服务器里
        Event event = new Event()
                .setTopic(Constant.TOPIC_PUBLISH)
                .setUserId(user.getId())
                .setEntityType(Constant.ENTITY_TYPE_ARTICLE)
                .setEntityId(article.getId());
        eventProducer.emitEvent(event);
        return "redirect:/index";
    }

    @GetMapping("/{id}")
    public String articleDetail(@PathVariable("id") int id, Model model, Page page) {
        //文章详情
        Article article = articleService.getById(id);
        User user = userService.findUserById(article.getUserId());
        //评论列表
        List<Map<String, Object>> commentVOList = commentService.listComments(id, page.getOffset() ,page.getLimit());
        //文章点赞数量
        Long likeCount = likeService.countLike(Constant.ENTITY_TYPE_ARTICLE,id);
        //点赞状态
        int likeStatus = loginUser.getUser() == null ? 0 : likeService.likeStatus(loginUser.getUser().getId(), Constant.ENTITY_TYPE_ARTICLE,id);
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

    @PostMapping("/top")
    @ResponseBody
    public String setTop(int id){
        articleService.setTop(id,1);
        Event event = new Event()
                .setTopic(Constant.TOPIC_PUBLISH)
                .setUserId(loginUser.getUser().getId())
                .setEntityType(Constant.ENTITY_TYPE_ARTICLE)
                .setEntityId(id);
        eventProducer.emitEvent(event);
        return ResultUtil.getJsonResult(0);
    }

    @PostMapping("/untop")
    @ResponseBody
    public String setunTop(int id){
        articleService.setTop(id,0);
        Event event = new Event()
                .setTopic(Constant.TOPIC_PUBLISH)
                .setUserId(loginUser.getUser().getId())
                .setEntityType(Constant.ENTITY_TYPE_ARTICLE)
                .setEntityId(id);
        eventProducer.emitEvent(event);
        return ResultUtil.getJsonResult(0);
    }

    @PostMapping("/marrow")
    @ResponseBody
    public String setMarrow(int id){
        articleService.setMarrow(id,1);
        Event event = new Event()
                .setTopic(Constant.TOPIC_PUBLISH)
                .setUserId(loginUser.getUser().getId())
                .setEntityType(Constant.ENTITY_TYPE_ARTICLE)
                .setEntityId(id);
        eventProducer.emitEvent(event);
        return ResultUtil.getJsonResult(0);
    }

    @PostMapping("/unmarrow")
    @ResponseBody
    public String setunMarrow(int id){
        articleService.setMarrow(id,0);
        Event event = new Event()
                .setTopic(Constant.TOPIC_PUBLISH)
                .setUserId(loginUser.getUser().getId())
                .setEntityType(Constant.ENTITY_TYPE_ARTICLE)
                .setEntityId(id);
        eventProducer.emitEvent(event);
        return ResultUtil.getJsonResult(0);
    }

    @PostMapping("/delete")
    @ResponseBody
    public String delete(int id){
        articleService.deleteById(id);
        //删除文章下的评论和回复
//        commentService.updateCommentByArticleId(id,1);
//        commentService.updateReplyByArticleId(id,1);
//        // 触发删博客事件
//        Event event = new Event()
//                .setTopic(Constant.TOPIC_DELETE)
//                .setUserId(loginUser.getUser().getId())
//                .setEntityType(Constant.ENTITY_TYPE_ARTICLE)
//                .setEntityId(id);
//        eventProducer.emitEvent(event);
        return ResultUtil.getJsonResult(0);
    }



}
