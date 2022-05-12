package com.example.blog.dao;

import com.example.blog.entity.Article;
import com.example.blog.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface ArticleMapper {

    List<Article> selectArticlesById(@Param("userId")int userId,@Param("offset") int offset,@Param("limit") int limit,
                                     @Param("sort")int sort);

    List<Article> selectIndexArticles(@Param("userId")int userId,@Param("offset") int offset,
                                        @Param("limit") int limit,
                                     @Param("sort")int sort);

    List<Article> selectArticlesByCategoryId(@Param("categoryId") Integer categoryId);

    int countIndexArticlesByCategory(@Param("categoryId") Integer categoryId,@Param("sort")int sort);

    List<Article> selectArticleListByCategoryId(@Param("categoryId") Integer categoryId,@Param("offset") int offset,
                                             @Param("limit") int limit,@Param("sort")int sort);

    int selectArticleRows(@Param("userId") int userId);

    int selectArticleDisplayRows(@Param("userId") int userId,@Param("sort")int sort);

    int insertArticle(Article article);

    int updateArticle(Article article);

    Article selectById(int id);

    int updateCommentCount(@Param("id")int id, @Param("commentCount")int commentCount);

    int deleteById(@Param("id")int id,@Param("status") Integer status,@Param("updateTime") Date updateTime);

    int delete(@Param("ids")List<Integer> ids,@Param("status") int status,@Param("updateTime") Date updateTime);

    int updateTop(@Param("id")int id,@Param("top") Integer top,@Param("updateTime") Date updateTime);

    int updateMarrow(@Param("id")int id, @Param("marrow")Integer marrow,@Param("updateTime") Date updateTime);

    int updateScore(@Param("id")int id, @Param("score")double score);

    int searchArticleCount(@Param("userId") int userId,@Param("title") String title,@Param("top") Integer top,
                           @Param("marrow")Integer marrow,@Param("categoryId")Integer categoryId );

    List<Article> searchArticleList(@Param("userId") int userId,@Param("title") String title,@Param("top") Integer top,
                                 @Param("marrow")Integer marrow,@Param("categoryId")Integer categoryId ,
                                    @Param("offset")Integer offset, @Param("limit")int limit);
}
