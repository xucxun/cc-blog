package com.example.blog.controller.admin;

import com.example.blog.common.Constant;
import com.example.blog.common.EventProducer;
import com.example.blog.entity.*;
import com.example.blog.service.ArticleService;
import com.example.blog.service.CategoryService;
import com.example.blog.util.BlogUtil;
import com.example.blog.util.LoginUser;
import com.example.blog.util.Result;
import com.example.blog.util.ResultUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/admin")
public class CategoryManageController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private EventProducer eventProducer;

    @Autowired
    private LoginUser loginUser;

    @GetMapping("/categoryManage")
    public String categoryManage(Model model, Page page) {
        // 设置分页信息
        page.setLimit(5);
        page.setRows(categoryService.countAllCategory());
        page.setPath("/admin/categoryManage");

        //查询类别列表
        List<Category> categories = categoryService.getAllCategory(page.getOffset(), page.getLimit());
        List<Map<String, Object>> list = new ArrayList<>();
        if (categories != null) {
            for (Category category : categories) {
                Map<String, Object> map = new HashMap<>();
                map.put("category", category);
                list.add(map);
            }
            model.addAttribute("categories", list);
        }
        model.addAttribute("categoryCount",categoryService.countAllCategory());
//        model.addAttribute("searchCategory",new Category());
        return "/admin/categoryManage";
    }

    @GetMapping("/categoryManage/search")
    public String searchCategory(Category category, Model model, Page page) {
        int categoryCount = categoryService.countSearchCategory(category.getName(),category.getDisplay());
        // 设置分页信息
        page.setLimit(5);
        page.setRows(categoryCount);
        page.setPath("/admin/categoryManage/search?name="+ category.getName()+"&display="+category.getDisplay());
        //查询搜索类别
        List<Category> categories = categoryService.searchCategoryList(category.getName(),category.getDisplay(),
                page.getOffset(), page.getLimit());

        List<Map<String, Object>> list = new ArrayList<>();
        if (categories != null) {
            for (Category searchCategory : categories) {
                Map<String, Object> map = new HashMap<>();
                map.put("category", searchCategory);
                list.add(map);
            }
            model.addAttribute("categories", list);
        }
        model.addAttribute("name",category.getName());
        model.addAttribute("display",category.getDisplay());
        model.addAttribute("categoryCount",categoryCount);
        return "/admin/categoryManage";
    }

    @PostMapping("/categoryManage/save")
    @ResponseBody
    public String saveCategory(String name,String description){
        if(StringUtils.isBlank(name) || StringUtils.isBlank(description)){
            return BlogUtil.getJsonResult(1, "类别或描述不能为空!");
        }
        Category isExist = categoryService.getCategoryByName(name);
        if(ObjectUtils.isNotEmpty(isExist)){
            return BlogUtil.getJsonResult(1, "类别名称重复!");
        }
        Category category = new Category();
        category.setName(name);
        category.setDescription(description);
        category.setStatus(0);
        category.setDisplay(0);
        category.setCreateTime(new Date());
        categoryService.addCategory(category);

        return BlogUtil.getJsonResult(0,"新增类别成功！");
    }

    @PostMapping("/categoryManage/display")
    @ResponseBody
    public String setDisplay(Integer id){
        categoryService.updateCategoryDisplay(id,0);
        List<Article> articles = articleService.findArticlesByCategoryId(id);
        List<Integer> ids = getArticleIds(articles);
        if (!ids.isEmpty()) {
            for (Integer articleId : ids) {
                Event event = new Event()
                        .setTopic(Constant.TOPIC_PUBLISH)
                        .setUserId(loginUser.getUser().getId())
                        .setEntityType(Constant.ENTITY_TYPE_ARTICLE)
                        .setEntityId(articleId);
                eventProducer.emitEvent(event);
            }
        }
        return BlogUtil.getJsonResult(0);
    }

    @PostMapping("/categoryManage/unDisplay")
    @ResponseBody
    public String setUnDisplay(Integer id){
        categoryService.updateCategoryDisplay(id,1);
        List<Article> articles = articleService.findArticlesByCategoryId(id);
        List<Integer> ids = getArticleIds(articles);
        if (!ids.isEmpty()) {
            for(Integer articleId: ids) {
                // 触发删博客事件
                Event event = new Event()
                        .setTopic(Constant.TOPIC_DELETE)
                        .setUserId(loginUser.getUser().getId())
                        .setEntityType(Constant.ENTITY_TYPE_ARTICLE)
                        .setEntityId(articleId);
                eventProducer.emitEvent(event);
            }
        }
        return BlogUtil.getJsonResult(0);
    }

    @PostMapping("/categoryManage/delete")
    @ResponseBody
    public String setDelete(Integer id){
        categoryService.updateCategoryStatus(id,1);
        List<Article> articles = articleService.findArticlesByCategoryId(id);
        List<Integer> ids = getArticleIds(articles);
        if (!ids.isEmpty()) {
            articleService.delete(ids);
        }
        return BlogUtil.getJsonResult(0,"删除类别成功！");
    }

    @GetMapping("/categoryManage/{id}")
    @ResponseBody
    public Result<Category> get(@PathVariable Integer id){
        Category category = categoryService.getCategory(id);
        return ResultUtil.ok(category);
    }

    @PostMapping("/categoryManage/edit")
    @ResponseBody
    public String edit(Integer id,String name,String description){
        if(StringUtils.isBlank(name) || StringUtils.isBlank(description)){
            return BlogUtil.getJsonResult(1, "类别或描述不能为空!");
        }
        Category isExist = categoryService.getOtherCategory(id,name);
        if(ObjectUtils.isNotEmpty(isExist)){
            return BlogUtil.getJsonResult(1, "类别名称重复!");
        }
        categoryService.updateCategory(id,name,description);
        return BlogUtil.getJsonResult(200,"修改类别成功");
    }


    private List<Integer> getArticleIds(List<Article> articles) {
        List<Integer> ids = new ArrayList<>();
        if (articles != null) {
            for (Article article : articles) {
                ids.add(article.getId());
            }
        }
        return ids;
    }
}
