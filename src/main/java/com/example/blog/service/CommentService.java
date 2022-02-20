package com.example.blog.service;

import com.example.blog.entity.Comment;

import java.util.List;
import java.util.Map;

public interface CommentService {

    Comment findCommentById(int id);

    /**
     * 分页查询评论
     */
    List<Comment> listByEntity(int entityType, int entityId, int offset, int limit);

    /**
     * 统计评论总数
     */
    int countAll(int entityType, int entityId);

    /**
     * 保存评论
     */
    int save(Comment comment);

    /**
     * 根据文章id分页查询评论列表
     */
    List<Map<String, Object>> listComments(int articleId, int offset, int limit);



}
