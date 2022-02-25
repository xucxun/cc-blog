package com.example.blog.service;

import com.example.blog.entity.Comment;

import java.util.List;
import java.util.Map;

public interface CommentService {

    /**
     * 根据ID查询评论
     * @param id 评论ID
     * @return 评论
     */
    Comment findCommentById(int id);

    /**
     * 统计评论总数
     * @param entityType 评论对象
     * @param entityId 评论对象id
     * @return 评论总数
     */
    int countAll(int entityType, int entityId);

    /**
     * 保存评论
     * @param comment 评论
     */
    int save(Comment comment);

    /**
     * 根据文章ID分页查询评论列表
     * @param articleId 文章ID
     * @param offset
     * @param limit
     * @return 评论列表
     */
    List<Map<String, Object>> listComments(int articleId, int offset, int limit);

    /**
     * 更新文章评论
     * @param entityId 文章ID
     * @param status
     */
    int updateCommentByArticleId(int entityId, int status);

    /**
     * 更新文章回复
     * @param entityId 文章ID
     * @param status
     */
    int updateReplyByArticleId(int entityId, int status);

}
