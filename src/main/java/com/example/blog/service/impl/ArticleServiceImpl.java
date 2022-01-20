package com.example.blog.service.impl;

import com.example.blog.dao.ArticleMapper;
import com.example.blog.entity.Article;
import com.example.blog.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
