package com.example.blog.controller;

import com.example.blog.entity.Article;
import com.example.blog.entity.Page;
import com.example.blog.entity.User;
import com.example.blog.service.ArticleService;
import com.example.blog.service.LikeService;
import com.example.blog.service.UserService;
import com.example.blog.common.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController{

    @Autowired
    private ArticleService articleService;

    @Autowired
    private UserService userService;

    @Autowired
    private LikeService likeService;

    @RequestMapping(value ={"/index","/"} , method = RequestMethod.GET)
    public String index(Model model, Page page) {
        page.setRows(articleService.findArticlesRows(0));
        page.setPath("/index");

        List<Article> list = articleService.findArticles(0, page.getOffset(), page.getLimit());
        List<Map<String, Object>> articleLists = new ArrayList<>();
        if (list != null) {
            for (Article article : list) {
                Map<String, Object> map = new HashMap<>();
                map.put("article", article);
                User user = userService.findUserById(article.getUserId());
                map.put("user", user);

                long likeCount = likeService.countLike(Constant.ENTITY_TYPE_ARTICLE, article.getId());
                map.put("likeCount", likeCount);
                articleLists.add(map);
            }
        }
        model.addAttribute("articleLists", articleLists);
        return "/index";
    }

   @GetMapping("/error")
    public String error() {
        return "/error/500";
    }

    @GetMapping("/denied")
    public String getDeniedPage() {
        return "/error/404";
    }
}
