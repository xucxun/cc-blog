package com.example.blog.quartz;

import com.example.blog.common.Constant;
import com.example.blog.entity.Article;
import com.example.blog.service.ArticleService;
import com.example.blog.service.ElasticsearchService;
import com.example.blog.service.LikeService;
import com.example.blog.util.RedisKeyUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ArticleScoreRefreshJob implements Job {

    private static final Logger logger = LoggerFactory.getLogger(ArticleScoreRefreshJob.class);

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private ElasticsearchService elasticsearchService;

    // Blog纪元
    private static final Date epoch;

    //初始化Blog纪元常量
    static {
        try {
            epoch = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2022-01-20 17:00:00");
        } catch (ParseException e) {
            throw new RuntimeException("初始化Blog纪元失败!", e);
        }
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        String redisKey = RedisKeyUtil.getArticleScoreKey();
        BoundSetOperations operations = redisTemplate.boundSetOps(redisKey);

        if (operations.size() == 0) {
            logger.info("[任务取消] 没有需要刷新的文章!");
            return;
        }

        logger.info("[任务开始] 正在刷新文章分数: " + operations.size());
        while (operations.size() > 0) {
            this.refresh((Integer) operations.pop());
        }
        logger.info("[任务结束] 文章分数刷新完毕!");
    }

    private void refresh(int articleId) {
        Article article = articleService.getById(articleId);

        if (article == null) {
            logger.error("该文章不存在: id = " + articleId);
            return;
        }
        if(article.getStatus() == 1){
            logger.error("文章已被删除");
            return;
        }

        // 是否精华
        boolean marrow = article.getMarrow() == 1;
        // 评论数量
        int commentCount = article.getCommentCount();
        // 文章点赞数量
        long likeCount = likeService.countLike(Constant.ENTITY_TYPE_ARTICLE,articleId);
        // 计算权重
        double w = (marrow ? 75 : 0) + commentCount * 10 + likeCount * 2;
        // 分数 = 博客权重 + 距离天数  log函数特点：随着x增加，y增加越来越慢
        //getTime()得到long型，越新发布的文章越有可能成为热门博客
        double score = Math.log10(Math.max(w, 1))
                + (article.getCreateTime().getTime() - epoch.getTime()) / (1000 * 3600 * 24);

        //更新文章分数
        articleService.updateScore(articleId,score);
        // 同步搜索数据
        article.setScore(score);
        elasticsearchService.saveArticle(article);

    }
}
