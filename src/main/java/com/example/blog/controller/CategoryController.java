package com.example.blog.controller;

import com.example.blog.entity.Category;
import com.example.blog.entity.Page;
import com.example.blog.service.ArticleService;
import com.example.blog.service.CategoryService;
import com.example.blog.service.LikeService;
import com.example.blog.service.UserService;
import com.example.blog.vo.ArticleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String category(@PathVariable(name = "id",required = false)Integer id, Model model,Page page,@RequestParam(name = "sort", defaultValue = "0") int sort){
        List<Category> categories = categoryService.getArticleCategory();
        if(id == null){
            id = categories.get(0).getId();
        }
        //设置分页
        page.setRows(articleService.countIndexArticlesByCategory(id,sort));
        page.setPath("/category/"+id+"?sort="+sort);
        //前台查询文章列表（类别为前台显示的）
        List<ArticleVO> list = articleService.findIndexArticleVOByCategory(id, page.getOffset(),page.getLimit(),sort);
        List<Map<String, Object>> articleLists = new ArrayList<>();
        if (list != null) {
            for (ArticleVO articleVO : list) {
                Map<String, Object> map = new HashMap<>();
                map.put("articleVO", articleVO);
                articleLists.add(map);
            }
        }
        model.addAttribute("categories", categories);
        model.addAttribute("articleLists", articleLists);
        model.addAttribute("activeCategoryId", id);
        model.addAttribute("sort",sort);
        return "/front/category";
    }


}
