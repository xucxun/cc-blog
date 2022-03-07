package com.example.blog.controller;

import com.example.blog.common.Constant;
import com.example.blog.entity.Article;
import com.example.blog.entity.Category;
import com.example.blog.entity.Page;
import com.example.blog.entity.User;
import com.example.blog.service.CategoryService;
import com.example.blog.service.ElasticsearchService;
import com.example.blog.service.LikeService;
import com.example.blog.service.UserService;
import com.example.blog.util.HtmlToPlainTextUtil;
import com.example.blog.util.MarkdownUtils;
import com.example.blog.vo.ArticleVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
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
//        if(!ObjectUtils.isEmpty(searchResults)){
//            for (Article article : searchResults) {
//                Map<String, Object> map = new HashMap<>();
//                String html = MarkdownUtils.markdownToHtmlExtensions(article.getContent());
//                article.setContent(HtmlToPlainTextUtil.convert(html));
//                map.put("article", article);
//                map.put("user", userService.findUserById(article.getUserId()));
//                map.put("likeCount", likeService.countLike(Constant.ENTITY_TYPE_ARTICLE, article.getId()));
//                map.put("category",categoryService.getCategory(article.getCategoryId()));
//                articleLists.add(map);
//            }
//        }
        List<ArticleVO> result = new ArrayList<>();
        List<Integer> userIds = new ArrayList<>();
        List<Integer> categoryIds = new ArrayList<>();
        if(!ObjectUtils.isEmpty(searchResults)){
            for (Article article : searchResults) {
                ArticleVO articleVO = new ArticleVO();
                BeanUtils.copyProperties(article, articleVO);
                String html = MarkdownUtils.markdownToHtmlExtensions(articleVO.getContent());
                articleVO.setContent(HtmlToPlainTextUtil.convert(html));
                articleVO.setLikeCount(likeService.countLike(Constant.ENTITY_TYPE_ARTICLE, articleVO.getId()));
                result.add(articleVO);
                userIds.add(articleVO.getUserId());
                categoryIds.add(articleVO.getCategoryId());
            }
        }
        if (!CollectionUtils.isEmpty(userIds)) {
            List<User> users = userService.findUserByIds(userIds);
            result.forEach(item -> {
                if (!CollectionUtils.isEmpty(users)) {
                    for (User user : users) {
                        if (item.getUserId() == user.getId()) {
                            item.setAuthorNickName(user.getNickName());
                            item.setAuthorAvatar(user.getAvatar());
                        }
                    }
                }
            });
        }

        if (!CollectionUtils.isEmpty(categoryIds)) {
            List<Category> categories = categoryService.getCategoryByIds(categoryIds);
            result.forEach(item -> {
                if (!CollectionUtils.isEmpty(categories)) {
                    for (Category category : categories) {
                        if (item.getCategoryId() == category.getId()) {
                            item.setCategoryName(category.getName());
                        }
                    }
                }
            });
        }

        if(!CollectionUtils.isEmpty(result)){
            for(ArticleVO articleVO : result){
                Map<String, Object> map = new HashMap<>();
                map.put("articleVO",articleVO);
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
