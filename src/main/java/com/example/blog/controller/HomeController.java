package com.example.blog.controller;

import com.example.blog.entity.Article;
import com.example.blog.entity.Page;
import com.example.blog.entity.User;
import com.example.blog.service.ArticleService;
import com.example.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private UserService userService;

    @RequestMapping(value ={"/index","/"} , method = RequestMethod.GET)
    public String index(Model model, Page page) {
        //SpringMVC会自动实例化Model和Page,并将Page注入Model.
        // 所以,在thymeleaf中可以直接访问Page对象中的数据.
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
                articleLists.add(map);
            }
        }
        model.addAttribute("articleLists", articleLists);
        return "/index";
    }
}
