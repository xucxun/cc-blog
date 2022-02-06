package com.example.blog.service.impl;

import com.example.blog.dao.ArticleMapper;
import com.example.blog.entity.Article;
import com.example.blog.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public List<Article> findArticles(int userId, int offset, int limit) {
        return articleMapper.selectArticlesById(userId,offset,limit);
    }

    @Override
    public int findArticlesRows(int userId) {
        return articleMapper.selectArticleRows(userId);
    }

    @Override
    public int save(Article article) {
        if (article == null) {
            throw new IllegalArgumentException("参数不能为空!");
        }
        article.setTitle(HtmlUtils.htmlEscape(article.getTitle()));
        article.setContent(HtmlUtils.htmlEscape(article.getContent()));
        return articleMapper.insertArticle(article);
    }

    @Override
    public Article getById(int id) {
        return articleMapper.selectById(id);
    }

    @Override
    public int updateCommentCount(int id, int commentCount) {
        return articleMapper.updateCommentCount(id,commentCount);
    }
}
