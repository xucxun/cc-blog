package com.example.blog.dao;

import com.example.blog.entity.Article;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ArticleMapper {

    List<Article> selectArticlesById(int userId, int offset, int limit);

    int selectArticleRows(@Param("userId") int userId);
}
