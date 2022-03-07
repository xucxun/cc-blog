package com.example.blog.controller;

import com.example.blog.entity.Article;
import com.example.blog.entity.Category;
import com.example.blog.entity.Page;
import com.example.blog.entity.User;
import com.example.blog.service.ArticleService;
import com.example.blog.service.CategoryService;
import com.example.blog.service.LikeService;
import com.example.blog.service.UserService;
import com.example.blog.common.Constant;
import com.example.blog.util.HtmlToPlainTextUtil;
import com.example.blog.util.MarkdownUtils;
import com.example.blog.vo.ArticleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.HtmlUtils;

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

    @Autowired
    private CategoryService categoryService;

    @RequestMapping(value ={"/index","/"} , method = RequestMethod.GET)
    public String index(Model model, Page page,@RequestParam(name = "sort", defaultValue = "0") int sort) {
        page.setLimit(10);
        page.setRows(articleService.findArticlesDisPlayRows(0));
        page.setPath("/index?sort="+sort);

//        List<Article> list = articleService.findIndexArticles(0, page.getOffset(), page.getLimit(),sort);
//        List<Map<String, Object>> articleLists = new ArrayList<>();
//        if (list != null) {
//            for (Article article : list) {
//                Map<String, Object> map = new HashMap<>();
//                String html = MarkdownUtils.markdownToHtmlExtensions(article.getContent());
//                article.setContent(HtmlToPlainTextUtil.convert(html));
//                map.put("article", article);
//                //用户
//                User user = userService.findUserById(article.getUserId());
//                map.put("user", user);
//                //文章点赞数
//                long likeCount = likeService.countLike(Constant.ENTITY_TYPE_ARTICLE, article.getId());
//                map.put("likeCount", likeCount);
//                //文章类别
//                Category category = categoryService.getCategory(article.getCategoryId());
//                map.put("category",category);
//                articleLists.add(map);
//            }
//        }

        List<ArticleVO> list = articleService.listIndexArticleVO(0, page.getOffset(), page.getLimit(), sort);
        List<Map<String,Object>> articleLists = new ArrayList<>();
        if(!CollectionUtils.isEmpty(list)){
            for (ArticleVO articleVO : list) {
                Map<String, Object> map = new HashMap<>();
                map.put("articleVO", articleVO);
                articleLists.add(map);
            }
        }
        model.addAttribute("articleLists", articleLists);
        model.addAttribute("sort",sort);
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
