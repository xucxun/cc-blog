package com.example.blog.common;

import com.alibaba.fastjson.JSONObject;
import com.example.blog.entity.Article;
import com.example.blog.entity.Event;
import com.example.blog.entity.Message;
import com.example.blog.service.ArticleService;
import com.example.blog.service.ElasticsearchService;
import com.example.blog.service.MessageService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class EventConsumer implements Constant{

    private static final Logger logger = LoggerFactory.getLogger(EventConsumer.class);

    @Autowired
    private MessageService messageService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private ElasticsearchService elasticsearchService;

    //消费评论、点赞、关注等系统通知事件
    @KafkaListener(topics = {TOPIC_COMMENT, TOPIC_LIKE, TOPIC_FOLLOW})
    public void handleNoticeEvent(ConsumerRecord record) {
        if (record == null || record.value() == null) {
            logger.error("消息的内容为空!");
            return;
        }

        Event event = JSONObject.parseObject(record.value().toString(), Event.class);
        if (event == null) {
            logger.error("消息格式错误!");
            return;
        }

        Message message = new Message();
        //由系统发送通知
        message.setSenderId(SYSTEM_ID);
        //用户接受通知
        message.setReceiverId(event.getEntityUserId());
        //通知类型
        message.setType(event.getTopic());
        message.setCreateTime(new Date());

        Map<String, Object> content = new HashMap<>();
        content.put("userId", event.getUserId());
        content.put("entityType", event.getEntityType());
        content.put("entityId", event.getEntityId());

        if (!event.getData().isEmpty()) {
            for (Map.Entry<String, Object> entry : event.getData().entrySet()) {
                content.put(entry.getKey(), entry.getValue());
            }
        }

        message.setContent(JSONObject.toJSONString(content));
        messageService.addMessage(message);
    }

    // 消费发博客事件
    @KafkaListener(topics = {TOPIC_PUBLISH})
    public void handlePublishEvent(ConsumerRecord record) {
        if (record == null || record.value() == null) {
            logger.error("消息的内容为空!");
            return;
        }

        Event event = JSONObject.parseObject(record.value().toString(), Event.class);
        if (event == null) {
            logger.error("消息格式错误!");
            return;
        }

        Article article = articleService.getById(event.getEntityId());
        //查询文章并存到es服务器
        elasticsearchService.saveArticle(article);
    }

    // 消费删除博客事件
    @KafkaListener(topics = {TOPIC_DELETE})
    public void handleDeleteEvent(ConsumerRecord record) {
        if (record == null || record.value() == null) {
            logger.error("消息的内容为空!");
            return;
        }

        Event event = JSONObject.parseObject(record.value().toString(), Event.class);
        if (event == null) {
            logger.error("消息格式错误!");
            return;
        }
        elasticsearchService.deleteArticle(event.getEntityId());

    }
}
