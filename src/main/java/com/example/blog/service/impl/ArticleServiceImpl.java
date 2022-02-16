package com.example.blog.service.impl;

import com.example.blog.dao.ArticleMapper;
import com.example.blog.entity.Article;
import com.example.blog.entity.User;
import com.example.blog.service.ArticleService;
import com.example.blog.util.HostHolder;
import com.example.blog.util.ResultUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.Date;
import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private HostHolder hostHolder;

    /**
     * 根据用户id分页查询文章列表
     */
    @Override
    public List<Article> findArticles(int userId, int offset, int limit) {
        return articleMapper.selectArticlesById(userId,offset,limit);
    }

    /**
     * 查询用户文章数
     */
    @Override
    public int findArticlesRows(int userId) {
        return articleMapper.selectArticleRows(userId);
    }

    /**
     * 根据id查询文章
     */
    @Override
    public Article getById(int id) {
        return articleMapper.selectById(id);
    }

    /**
     * 保存文章
     */
    @Override
    public String add(String title, String content) {
        User user = hostHolder.getUser();
        if (user == null) {
            return ResultUtil.getJsonResult(403, "您还没有登录哦!");
        }
        if(StringUtils.isBlank(title) || StringUtils.isBlank(content)){
            return ResultUtil.getJsonResult(1, "标题或内容不能为空!");
        }

        Article article = new Article();
        article.setUserId(user.getId());
        article.setTitle(HtmlUtils.htmlEscape(title));
        article.setContent(HtmlUtils.htmlEscape(content));
        article.setCreateTime(new Date());
        articleMapper.insertArticle(article);

        return ResultUtil.getJsonResult(0, "文章发布成功!");
    }
}
