package com.example.blog.service.impl;

import com.example.blog.common.EventProducer;
import com.example.blog.dao.ArticleMapper;
import com.example.blog.dao.CommentMapper;
import com.example.blog.entity.Article;
import com.example.blog.entity.User;
import com.example.blog.service.ArticleService;
import com.example.blog.util.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import java.util.Date;
import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private LoginUser loginUser;

    @Autowired
    private EventProducer eventProducer;

    @Value("${caffeine.max-size}")
    private int maxSize;

    @Value("${caffeine.expire-seconds}")
    private int expireSeconds;

//    // Caffeine核心接口: Cache, LoadingCache, AsyncLoadingCache
//
//    // 热门文章列表缓存。缓存都是按key缓存value
//    private LoadingCache<String, List<Article>> articleListCache;
//
//    // 热门文章总数缓存
//    private LoadingCache<Integer, Integer> articleRowsCache;
//
//    //@PostConstruct表示对象创建的时候会执行这个方法
//    @Override
//    @PostConstruct
//    public void init() {
//
//        articleListCache = Caffeine.newBuilder()
//                .maximumSize(maxSize)
//                .expireAfterWrite(expireSeconds, TimeUnit.SECONDS)
//                .build(new CacheLoader<String, List<Article>>() {
//                    @Nullable
//                    @Override
//                    public List<Article> load(@NonNull String key) throws Exception {
//                        if (key == null || key.length() == 0) {
//                            throw new IllegalArgumentException("参数错误!");
//                        }
//
//                        String[] params = key.split(":");
//                        if (params == null || params.length != 2) {
//                            throw new IllegalArgumentException("参数错误!");
//                        }
//
//                        int offset = Integer.valueOf(params[0]);
//                        int limit = Integer.valueOf(params[1]);
//
//                        return articleMapper.selectArticlesById(0, offset, limit, 1);
//                    }
//                });
//        // 初始化热门文章总数缓存
//       articleRowsCache = Caffeine.newBuilder()
//                // 最大本地缓存数据的条数
//                .maximumSize(maxSize)
//                // 本地缓存数据的过期时间
//                .expireAfterWrite(expireSeconds, TimeUnit.SECONDS)
//                .build(new CacheLoader<Integer, Integer>() {
//                    @Override
//                    public @Nullable Integer load(@NonNull Integer key) throws Exception {
//                        // 从数据库查数据,获取后将数据放入本地缓存
//                        return articleMapper.selectArticleRows(key);
//                    }
//                });
//
//
//    }

    @Override
    public List<Article> findIndexArticles(int userId, int offset, int limit, int sort) {
        return articleMapper.selectIndexArticles(userId,offset,limit,sort);
    }

    /**
     * 根据用户id分页查询文章列表
     */
    @Override
    public List<Article> findArticles(int userId, int offset, int limit,int sort) {
        //使用articleListCache缓存。使用缓存后实时性可能会降低，但从缓存中查询速度提高
//        if (userId == 0 && sort == 1) {
//            return articleListCache.get(offset + ":" + limit);
//        }
        return articleMapper.selectArticlesById(userId,offset,limit,sort);
    }

    /**
     * 查询用户文章数
     */
    @Override
    public int findArticlesRows(int userId) {
//        if (userId == 0) {
//            return articleRowsCache.get(userId);
//        }
        return articleMapper.selectArticleRows(userId);
    }

    @Override
    public int findArticlesDisPlayRows(int userId) {
        return articleMapper.selectArticleDisplayRows(userId);
    }


    /**
     * 根据id查询文章
     */
    @Override
    public Article getById(int id) {
        return articleMapper.selectById(id);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public int add(Article article) {
        User user = loginUser.getUser();
        if (article == null) {
            throw new IllegalArgumentException("参数不能为空!");
        }

        article.setUserId(user.getId());
        article.setCreateTime(new Date());
        article.setStatus(0);
        article.setMarrow(0);
        article.setTop(0);
        // 转义HTML标记,不会将标签识别出来。过滤标签
        article.setTitle(HtmlUtils.htmlEscape(article.getTitle()));
        article.setContent(HtmlUtils.htmlEscape(article.getContent()));

        return articleMapper.insertArticle(article);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(Article article) {
        article.setUpdateTime(new Date());
        article.setStatus(0);
        return articleMapper.updateArticle(article);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(int id) {
        return articleMapper.deleteById(id,1,new Date());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int setTop(int id,Integer top) {
       return articleMapper.updateTop(id,top,new Date());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int setMarrow(int id, Integer marrow) {
        return articleMapper.updateMarrow(id,marrow,new Date());
    }

    @Override
    public int updateScore(int id, double score) {
        return articleMapper.updateScore(id,score);
    }

    @Override
    public int countSearchArticle(int userId, String title, Integer top, Integer marrow) {
        return articleMapper.searchArticleCount(userId,title,top,marrow);
    }

    @Override
    public List<Article> searchArticleList(int userId, String title, Integer top, Integer marrow, int offset, int limit) {
        return articleMapper.searchArticleList(userId,title,top,marrow,offset,limit);
    }

    @Override
    public List<Article> findArticlesByCategoryId(Integer categoryId) {
        return articleMapper.selectArticlesByCategoryId(categoryId);
    }

    @Override
    public int countIndexArticlesByCategory(Integer categoryId) {
        return articleMapper.countIndexArticlesByCategory(categoryId);
    }

    @Override
    public List<Article> findArticlesByCategoryId(Integer categoryId, int offset, int limit) {
        return articleMapper.selectArticleListByCategoryId(categoryId,offset,limit);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(List<Integer> ids) {
        Date date = new Date();
        articleMapper.delete(ids,1,date);
//
//        for(Integer id: ids) {
//            //删除文章下的评论和回复
//            commentMapper.updateCommentByArticleId(id,1,date);
//            commentMapper.updateReplyByArticleId(id,1,date);
//            // 触发删博客事件
//            Event event = new Event()
//                    .setTopic(Constant.TOPIC_DELETE)
//                    .setUserId(loginUser.getUser().getId())
//                    .setEntityType(Constant.ENTITY_TYPE_ARTICLE)
//                    .setEntityId(id);
//            eventProducer.emitEvent(event);
//        }
    }
}
