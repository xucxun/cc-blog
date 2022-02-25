package com.example.blog.util;

public class RedisKeyUtil {

    private static final String SPLIT = ":";
    private static final String LIKE = "like";
    private static final String USER_LIKE = "like:user";
    private static final String FOLLOWING = "following";
    private static final String FOLLOWER = "follower";
    private static final String KAPTCHA = "kaptcha";
    private static final String TICKET = "ticket";
    private static final String USER = "user";

    /**
     * 对文章或评论的赞
     * @param entityType 1-文章，2-评论
     * @param entityId 点赞的用户id
     * @return
     */
    public static String getLikeKey(int entityType, int entityId) {
        return LIKE + SPLIT + entityType + SPLIT + entityId;
    }

    /**
     * 对用户的赞
     * @param userId
     * @return
     */
    public static String getUserLikeKey(int userId) {
        return USER_LIKE + SPLIT + userId;
    }

    /**
     * 用户关注的实体
     * @param userId
     * @param entityType
     * @return
     */
    public static String getFollowingKey(int userId, int entityType) {
        return FOLLOWING + SPLIT + userId + SPLIT + entityType;
    }

    /**
     *  某个实体的粉丝
     * @param entityType
     * @param entityId
     * @return
     */
    public static String getFollowerKey(int entityType, int entityId) {
        return FOLLOWER + SPLIT + entityType + SPLIT + entityId;
    }

    // 登录验证码，验证码的归属者
    public static String getKaptchaKey(String belonger) {
        return KAPTCHA + SPLIT + belonger;
    }

    // 登录的凭证
    public static String getTicketKey(String ticket) {
        return TICKET + SPLIT + ticket;
    }

    // 用户
    public static String getUserKey(int userId) {
        return USER + SPLIT + userId;
    }
    
}
