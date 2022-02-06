package com.example.blog.service;

import com.example.blog.entity.Comment;

import java.util.List;

public interface CommentService {

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


}
