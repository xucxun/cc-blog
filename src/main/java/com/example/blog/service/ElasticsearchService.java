package com.example.blog.service;

import com.example.blog.entity.Article;
import org.springframework.data.domain.Page;

public interface ElasticsearchService {
    /**
     * 保存文章到es
     */
    void saveArticle(Article article);

    /**
     * 从es服务器删除文章
     */
    void deleteArticle(int id);

    /**
     * 从es搜索文章并高亮显示搜索关键字
     */
    Page<Article> searchArticle(String key, int current, int limit);


}
