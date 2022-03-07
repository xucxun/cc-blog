package com.example.blog.service;

import java.util.List;

public interface LikeService {

    /**
     * 对文章或评论点赞
     */
    void like(int userId, int entityType, int entityId, int entityUserId);

    /**
     * 文章或评论点赞数量
     */
    long countLike(int entityType, int entityId);


    /**
     * 当前用户对该文章或评论的点赞状态
     */
    int likeStatus(int userId, int entityType, int entityId);

    /**
     * 当前用户获得的赞
     */
    int userLikeCount(int userId);

}
