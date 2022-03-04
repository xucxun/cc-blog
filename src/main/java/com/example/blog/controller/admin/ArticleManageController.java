package com.example.blog.controller.admin;

import com.example.blog.common.EventProducer;
import com.example.blog.entity.Article;
import com.example.blog.entity.Page;
import com.example.blog.entity.User;
import com.example.blog.service.ArticleService;
import com.example.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class ArticleManageController {
    @Autowired
    private ArticleService articleService;

    @GetMapping("/articleManage")
    public String userManage(Model model, Page page) {
        // 设置分页信息
        page.setLimit(5);
        page.setRows(articleService.findArticlesRows(0));
        page.setPath("/admin/articleManage");

        //按创建时间获取文章列表
        List<Article> articles = articleService.findArticles(0, page.getOffset(), page.getLimit(), 3);
        List<Map<String, Object>> list = new ArrayList<>();
        if (articles != null) {
            for (Article article : articles) {
                Map<String, Object> map = new HashMap<>();
                map.put("article", article);
                list.add(map);
            }
            model.addAttribute("articles", list);
        }
        model.addAttribute("articleCount",articleService.findArticlesRows(0));
        model.addAttribute("searchArticle",new Article());
        return "/admin/articleManage";
    }

    /**
     * 根据用户id,标题、置顶、加精查询文章列表
     * @return
     */
    @GetMapping("/articleManage/search")
    public String searchArticle(Article article, Model model, Page page) {
        if(article.getUserId()==null){
            article.setUserId(0);
        }
        int articleCount = articleService.countSearchArticle(article.getUserId(),article.getTitle(),article.getTop(),
                article.getMarrow());
        // 设置分页信息
        page.setLimit(5);
        page.setRows(articleCount);
       page.setPath("/admin/articleManage/search?userId="+article.getUserId()+ "&title="+article.getTitle()+"&top="+article.getTop()+
               "&marrow="+article.getMarrow());
        List<Article> articles = articleService.searchArticleList(article.getUserId(),article.getTitle(),
                article.getTop(),article.getMarrow(),page.getOffset(),page.getLimit());
        List<Map<String, Object>> list = new ArrayList<>();
        if (articles != null) {
            for (Article searchArticle : articles) {
                Map<String, Object> map = new HashMap<>();
                map.put("article", searchArticle);
                list.add(map);
            }
            model.addAttribute("articles", list);
        }


        model.addAttribute("userId",article.getUserId());
        model.addAttribute("title",article.getTitle());
        model.addAttribute("top",article.getTop());
        model.addAttribute("marrow",article.getMarrow());
        model.addAttribute("searchArticle",article);
        model.addAttribute("articleCount",articleCount);
        return "/admin/articleManage";
    }

}
