package com.example.blog.service.impl;

import com.example.blog.common.Constant;
import com.example.blog.common.EventProducer;
import com.example.blog.dao.ArticleMapper;
import com.example.blog.entity.Article;
import com.example.blog.entity.Event;
import com.example.blog.entity.User;
import com.example.blog.service.ArticleService;
import com.example.blog.util.LoginUser;
import com.example.blog.util.ResultUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import java.util.Date;
import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private LoginUser loginUser;

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


    @Override
    @Transactional(rollbackFor = Exception.class)
    public int add(Article article) {
        User user = loginUser.getUser();
        if (article == null) {
            throw new IllegalArgumentException("参数不能为空!");
        }

        article.setUserId(user.getId());
        article.setCreateTime(new Date());
        article.setStatus(0);
        article.setMarrow(0);
        article.setTop(0);
        // 转义HTML标记,不会将标签识别出来。过滤标签
        article.setTitle(HtmlUtils.htmlEscape(article.getTitle()));
        article.setContent(HtmlUtils.htmlEscape(article.getContent()));

        return articleMapper.insertArticle(article);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(Article article) {
        article.setUpdateTime(new Date());
        article.setStatus(0);
        return articleMapper.updateArticle(article);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(int id) {
        return articleMapper.delete(id,1,new Date());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int setTop(int id,Integer top) {
       return articleMapper.updateTop(id,top,new Date());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int setMarrow(int id, Integer marrow) {
        return articleMapper.updateMarrow(id,marrow,new Date());
    }
}
