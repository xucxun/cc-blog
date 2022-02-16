package com.example.blog.service.impl;

import com.example.blog.entity.User;
import com.example.blog.service.FollowService;
import com.example.blog.service.UserService;
import com.example.blog.util.BlogConstant;
import com.example.blog.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FollowServiceImpl implements FollowService, BlogConstant {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private UserService userService;

    /**
     * 当前用户关注其他用户
     */
    @Override
    public void follow(int userId, int entityType, int entityId) {
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {

                String followingKey = RedisKeyUtil.getFollowingKey(userId, entityType);
                String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
                operations.multi();
                //添加当前用户关注的用户
                operations.opsForZSet().add(followingKey, entityId, System.currentTimeMillis());
                //添加关注当前用户的粉丝
                operations.opsForZSet().add(followerKey, userId, System.currentTimeMillis());

                return operations.exec();
            }
        });
    }

    /**
     * 当前用户取消关注用户
     */
    public void unfollow(int userId, int entityType, int entityId) {
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String followingKey = RedisKeyUtil.getFollowingKey(userId, entityType);
                String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);

                operations.multi();

                operations.opsForZSet().remove(followingKey, entityId);
                operations.opsForZSet().remove(followerKey, userId);

                return operations.exec();
            }
        });
    }

    /**
     * 当前用户关注用户数量
     */
    @Override
    public long countFollowing(int userId, int entityType) {
        String followingKey = RedisKeyUtil.getFollowingKey(userId, entityType);
        return redisTemplate.opsForZSet().zCard(followingKey);
    }

    /**
     * 当前用户的粉丝数量
     */
    @Override
    public long countFollower(int entityType, int entityId) {
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        return redisTemplate.opsForZSet().zCard(followerKey);
    }

    /**
     * 当前用户是否已关注该用户
     */
    @Override
    public boolean isFollowed(int userId, int entityType, int entityId) {
        String followingKey = RedisKeyUtil.getFollowingKey(userId, entityType);
        return redisTemplate.opsForZSet().score(followingKey, entityId) != null;
    }

    /**
     * 我关注的人列表
     */
    @Override
    public List<Map<String, Object>> listFollowings(int userId, int offset, int limit) {
        String followingKey = RedisKeyUtil.getFollowingKey(userId, ENTITY_TYPE_USER);
        //获取关注的用户id集合
        Set<Integer> followingUserIds = redisTemplate.opsForZSet().reverseRange(followingKey, offset, offset + limit - 1);
        if (followingUserIds == null) {
            return null;
        }
        List<Map<String, Object>> list = new ArrayList<>();
        for (Integer followingUserId : followingUserIds) {
            Map<String, Object> map = new HashMap<>();
            User followingUser = userService.findUserById(followingUserId);
            map.put("followingUser", followingUser);
            //获取关注时间
            Double score = redisTemplate.opsForZSet().score(followingKey, followingUserId);
            map.put("followingTime", new Date(score.longValue()));
            list.add(map);
        }

        return list;

    }

    /**
     * 关注我的人列表
     */
    @Override
    public List<Map<String, Object>> listFollowers(int userId, int offset, int limit) {
        String followerKey = RedisKeyUtil.getFollowerKey(ENTITY_TYPE_USER,userId);
        //redisTemplate返回的set集合是有序的
        //获取关注我的用户id集合
        Set<Integer> followerUserIds = redisTemplate.opsForZSet().reverseRange(followerKey, offset, offset + limit - 1);
        if (followerUserIds == null) {
            return null;
        }
        List<Map<String, Object>> list = new ArrayList<>();
        for (Integer followerUserId : followerUserIds) {
            Map<String, Object> map = new HashMap<>();
            User followerUser = userService.findUserById(followerUserId);
            map.put("followerUser", followerUser);
            //关注时间
            Double score = redisTemplate.opsForZSet().score(followerKey, followerUserId);
            map.put("followerTime", new Date(score.longValue()));
            list.add(map);
        }

        return list;
    }
}
