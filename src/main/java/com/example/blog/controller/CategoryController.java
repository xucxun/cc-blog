package com.example.blog.controller;

import com.example.blog.common.Constant;
import com.example.blog.entity.Article;
import com.example.blog.entity.Category;
import com.example.blog.entity.Page;
import com.example.blog.entity.User;
import com.example.blog.service.ArticleService;
import com.example.blog.service.CategoryService;
import com.example.blog.service.LikeService;
import com.example.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private UserService userService;

    @Autowired
    private LikeService likeService;

    @GetMapping(value = {"/category/{id}","/category"})
    public String category(@PathVariable(required = false)Integer id, Model model , Page page){

        //设置分页
        page.setRows(articleService.countIndexArticlesByCategory(id));
        page.setPath("/cateegory/"+id);
        List<Category> categories = categoryService.getArticleCategory();
        if(id == null){
            id = categories.get(0).getId();
        }
        //前台查询文章列表（类别为前台显示的）
        List<Article> list = articleService.findArticlesByCategoryId(id, page.getOffset(),page.getLimit());
        List<Map<String, Object>> articleLists = new ArrayList<>();
        if (list != null) {
            for (Article article : list) {
                Map<String, Object> map = new HashMap<>();
                map.put("article", article);
                //用户
                User user = userService.findUserById(article.getUserId());
                map.put("user", user);
                //文章点赞数
                long likeCount = likeService.countLike(Constant.ENTITY_TYPE_ARTICLE, article.getId());
                map.put("likeCount", likeCount);
                //文章类别
                Category category = categoryService.getCategory(article.getCategoryId());
                map.put("category",category);
                articleLists.add(map);
            }
        }
        model.addAttribute("categories", categories);
        model.addAttribute("articleLists", articleLists);
        model.addAttribute("activeCategoryId", id);
        return "/front/category";
    }


}
