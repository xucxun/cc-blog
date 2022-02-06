package com.example.blog.service.impl;

import com.example.blog.dao.CommentMapper;
import com.example.blog.entity.Comment;
import com.example.blog.service.ArticleService;
import com.example.blog.service.CommentService;
import com.example.blog.util.BlogConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService , BlogConstant {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private ArticleService articleService;

    @Override
    public List<Comment> listByEntity(int entityType, int entityId, int offset, int limit) {
        return commentMapper.selectCommentsByEntity(entityType,entityId,offset,limit);
    }

    @Override
    public int countAll(int entityType, int entityId) {
        return commentMapper.selectCountByEntity(entityType,entityId);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int save(Comment comment) {
        if (comment == null) {
            throw new IllegalArgumentException("参数不能为空!");
        }
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));   //让<>不会转换成html语言
        int row = commentMapper.insertComment(comment);
        // 更新博客评论数量
        if (comment.getEntityType() == ENTITY_TYPE_ARTICLE) {
            int count = commentMapper.selectCountByEntity(comment.getEntityType(), comment.getEntityId());
            articleService.updateCommentCount(comment.getEntityId(), count);
        }
        return row;
    }

}
