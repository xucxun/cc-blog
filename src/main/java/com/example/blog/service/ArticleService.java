package com.example.blog.service;

import com.example.blog.entity.Article;

import java.util.List;

public interface ArticleService {

    /**
     * 根据用户ID分页查询文章列表
     * @param userId 用户ID
     * @return 文章列表
     */
    List<Article> findArticles(int userId, int offset, int limit);

    /**
     * 查询用户文章数
     * @param userId 用户ID
     * @return
     */
    int findArticlesRows(int userId);

    /**
     * 根据ID查询文章
     * @param id 文章ID
     * @return
     */
    Article getById(int id);

    /**
     * 保存文章
     * @param article 文章
     * @return
     */
    int add(Article article);

    /**
     * 更新文章
     * @param article 文章
     * @return
     */
    int update(Article article);

    /**
     *  删除文章
     * @param id 文章ID
     * @return
     */
    int deleteById(int id);

    /**
     * 置顶文章
     * @param id 文章ID
     * @param top 是否置顶
     */
    int setTop(int id,Integer top);

    /**
     * 加精文章
     * @param id 文章ID
     * @param marrow 是否加精
     */
    int setMarrow(int id,Integer marrow);
}
