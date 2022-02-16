package com.example.blog.service;

import java.util.List;
import java.util.Map;

public interface FollowService {

    /**
     * 当前用户关注用户
     */
    void follow(int userId, int entityType, int entityId);

    /**
     * 当前用户取消关注用户
     */
    void unfollow(int userId, int entityType, int entityId);

    /**
     * 当前用户关注用户数量
     */
    long countFollowing(int userId, int entityType);

    /**
     * 文章或用户的粉丝数量
     */
    long countFollower(int entityType, int entityId);

    /**
     *  当前用户是否已关注该用户
     */
    boolean isFollowed(int userId, int entityType, int entityId);

    /**
     * 我关注的人列表
     */
    List<Map<String, Object>> listFollowings(int userId, int offset, int limit);

    /**
     * 关注我的人列表
     */
    List<Map<String, Object>> listFollowers(int userId, int offset, int limit);
}
