package com.example.blog.common;

public interface Constant {

    /**
     * 激活成功
     */
    int ACTIVATION_SUCCESS = 0;

    /**
     * 重复激活
     */
    int ACTIVATION_REPEAT = 1;

    /**
     * 激活失败
     */
    int ACTIVATION_FAILURE = 2;

    /**
     * 默认状态的登录凭证的超时时间，12小时
     */
    int DEFAULT_EXPIRED_SECONDS = 3600 * 12;

    /**
     * 记住状态的登录凭证超时时间，7天
     */
    int REMEMBER_EXPIRED_SECONDS = 3600 * 24 * 7;

    /**
     * 实体类型: 文章
     */
    int ENTITY_TYPE_ARTICLE = 1;

    /**
     * 实体类型: 评论
     */
    int ENTITY_TYPE_COMMENT = 2;

    /**
     * 实体类型: 用户
     */
    int ENTITY_TYPE_USER = 3;

    /**
     * 主题: 评论
     */
    String TOPIC_COMMENT = "comment";

    /**
     * 主题: 点赞
     */
    String TOPIC_LIKE = "like";

    /**
     * 主题: 关注
     */
    String TOPIC_FOLLOW = "follow";

    /**
     * 发布文章
     */
    String TOPIC_PUBLISH = "publish";

    /**
     * 系统用户ID   这个是数据库中一定要存在的用户，用于发送系统通知。系统通知账号system，密码system，一般不登录这个账户，使用这个账户发私信会有bug
     */
    int SYSTEM_ID = 1;

}
