package com.example.blog.service;

import com.example.blog.entity.Article;

import java.util.List;

public interface ArticleService {

    /**
     * 根据用户id分页查询文章
     */
    List<Article> findArticles(int userId, int offset, int limit);

    /**
     * 查询用户文章数
     */
    int findArticlesRows(int userId);
}
