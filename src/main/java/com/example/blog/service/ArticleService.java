package com.example.blog.service;

import com.example.blog.entity.Article;
import com.example.blog.entity.User;
import com.example.blog.vo.ArticleVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ArticleService {

    /**
     * 初始化热门文章列表缓存
     */
//    void init();

    /**
     * 前台展示分页查询文章列表
     * @param userId 用户ID
     * @return 文章列表
     */
    List<Article> findIndexArticles(int userId, int offset, int limit,int sort);

    /**
     * 优化 前台展示文章列表
     * @param userId
     * @param offset
     * @param limit
     * @param sort
     * @return
     */
    List<ArticleVO> listIndexArticleVO(int userId, int offset, int limit,int sort);


    /**
     * 查询用户文章列表
     * @param userId
     * @param offset
     * @param limit
     * @param sort
     * @return
     */
    List<Article> findArticles(int userId, int offset, int limit,int sort);

    List<ArticleVO> findArticleVOs(int userId, int offset, int limit,int sort);

    /**
     * 查询用户文章数
     * @param userId 用户ID
     * @return
     */
    int findArticlesRows(int userId);


    /**
     * 查询用户文章数
     * @param userId 用户ID
     * @return
     */
    int findArticlesDisPlayRows(int userId);


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

    /**
     * 更新文章分数
     * @param id
     * @param score
     * @return
     */
    int updateScore(int id, double score);

    /**
     * 搜索文章
     */
    int countSearchArticle(int userId,String title,Integer top,Integer marrow,Integer categoryId);

    /**
     * 后台搜索分页文章列表
     */
    List<Article> searchArticleList(int userId, String title, Integer top, Integer marrow,Integer categoryId,
                                    int offset, int limit);

    /**
     * 根据分类查询所有文章列表
     * @param categoryId
     * @return
     */
    List<Article> findArticlesByCategoryId(Integer categoryId);

    /**
     * 前台类别下的文章数
     * @param categoryId
     * @return
     */
    int countIndexArticlesByCategory(Integer categoryId);

    /**
     * 前台显示根据类别查询文章
     * @param categoryId
     * @param offset
     * @param limit
     * @return
     */
    List<Article> findArticlesByCategoryId(Integer categoryId,int offset,int limit);

    List<ArticleVO> findIndexArticleVOByCategory(Integer categoryId,int offset,int limit);

    /**
     * 批量删除文章
     * @param ids
     */
    void delete(List<Integer> ids);
}
