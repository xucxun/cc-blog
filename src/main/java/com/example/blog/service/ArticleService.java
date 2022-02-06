package com.example.blog.service;

import com.example.blog.entity.Article;

import java.util.List;

public interface ArticleService {

    /**
     * 根据用户id分页查询文章列表
     */
    List<Article> findArticles(int userId, int offset, int limit);

    /**
     * 查询用户文章数
     */
    int findArticlesRows(int userId);

    /**
     * 保存文章
     */
    int save(Article article);
    /**
     * 根据id查询文章
     */
    Article getById(int id);

    /**
     * 更新文章评论数
     */
    int updateCommentCount(int id, int commentCount);
}
