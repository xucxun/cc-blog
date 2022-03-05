package com.example.blog.controller;

import com.example.blog.common.Constant;
import com.example.blog.entity.Article;
import com.example.blog.entity.Page;
import com.example.blog.service.CategoryService;
import com.example.blog.service.ElasticsearchService;
import com.example.blog.service.LikeService;
import com.example.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class SearchController {

    @Autowired
    private ElasticsearchService elasticsearchService;

    @Autowired
    private UserService userService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/search")
    public String search(String key, Model model, Page page){
        // 搜索博客
        org.springframework.data.domain.Page<Article> searchResults = elasticsearchService.searchArticle(key,
                page.getCurrent() - 1, page.getLimit());
        List<Map<String, Object>> articleLists = new ArrayList<>();
        if(!ObjectUtils.isEmpty(searchResults)){
            for (Article article : searchResults) {
                Map<String, Object> map = new HashMap<>();
                map.put("article", article);
                map.put("user", userService.findUserById(article.getUserId()));
                map.put("likeCount", likeService.countLike(Constant.ENTITY_TYPE_ARTICLE, article.getId()));
                map.put("category",categoryService.getCategory(article.getCategoryId()));
                articleLists.add(map);
            }
        }
        model.addAttribute("articleLists", articleLists);
        model.addAttribute("key", key);

        page.setPath("/search?key=" + key);
        page.setRows(searchResults == null ? 0 : (int) searchResults.getTotalElements());

        model.addAttribute("searchCount",searchResults == null ? 0 : (int) searchResults.getTotalElements());

        return "front/search";
    }


}
