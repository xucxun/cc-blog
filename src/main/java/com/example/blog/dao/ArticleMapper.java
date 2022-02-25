package com.example.blog.dao;

import com.example.blog.entity.Article;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface ArticleMapper {

    List<Article> selectArticlesById(int userId, int offset, int limit);

    int selectArticleRows(@Param("userId") int userId);

    int insertArticle(Article article);

    int updateArticle(Article article);

    Article selectById(int id);

    int updateCommentCount(@Param("id")int id, @Param("commentCount")int commentCount);

    int delete(@Param("id")int id,@Param("status") Integer status,@Param("updateTime") Date updateTime);

    int updateTop(@Param("id")int id,@Param("top") Integer top,@Param("updateTime") Date updateTime);

    int updateMarrow(@Param("id")int id, @Param("marrow")Integer marrow,@Param("updateTime") Date updateTime);
}
