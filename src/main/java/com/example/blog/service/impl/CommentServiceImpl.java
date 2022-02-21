package com.example.blog.service.impl;

import com.example.blog.dao.ArticleMapper;
import com.example.blog.dao.CommentMapper;
import com.example.blog.dao.UserMapper;
import com.example.blog.entity.Comment;
import com.example.blog.entity.Event;
import com.example.blog.entity.User;
import com.example.blog.service.CommentService;
import com.example.blog.service.LikeService;
import com.example.blog.common.Constant;
import com.example.blog.util.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import java.util.*;

@Service
public class CommentServiceImpl implements CommentService{

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private LoginUser loginUser;

    @Autowired
    private LikeService likeService;


    @Override
    public Comment findCommentById(int id) {
        return commentMapper.selectCommentById(id);
    }

    /**
     * 分页查询评论
     */
    @Override
    public List<Comment> listByEntity(int entityType, int entityId, int offset, int limit) {
        return commentMapper.selectCommentsByEntity(entityType,entityId,offset,limit);
    }

    /**
     * 统计评论总数
     */
    @Override
    public int countAll(int entityType, int entityId) {
        return commentMapper.selectCountByEntity(entityType,entityId);
    }

    @Override
    public List<Map<String, Object>> listComments(int articleId, int offset, int limit) {
        //获取文章所有评论
        List<Comment> commentList = commentMapper.selectCommentsByEntity(Constant.ENTITY_TYPE_ARTICLE,articleId,
                offset,limit);
        //评论列表
        List<Map<String, Object>> commentVOList = new ArrayList<>();
        if (commentList != null) {
            for (Comment comment : commentList) {
                Map<String, Object> commentVO = new HashMap<>();
                commentVO.put("comment", comment);
                commentVO.put("user", userMapper.selectById((comment.getUserId())));
                Long likeCount = likeService.countLike(Constant.ENTITY_TYPE_COMMENT, comment.getId());
                commentVO.put("likeCount", likeCount);
                int likeStatus = loginUser.getUser() == null ? 0 : likeService.likeStatus(loginUser.getUser().getId()
                        , Constant.ENTITY_TYPE_COMMENT, comment.getId());
                commentVO.put("likeStatus", likeStatus);
                //获取单当前文章所有评论的回复
                List<Comment> replyList = commentMapper.selectCommentsByEntity(Constant.ENTITY_TYPE_COMMENT,
                        comment.getId(), 0, Integer.MAX_VALUE);
                // 回复列表
                List<Map<String, Object>> replyVOList = new ArrayList<>();
                if (replyList != null) {
                    for (Comment reply : replyList) {
                        Map<String, Object> replyVO = new HashMap<>();
                        replyVO.put("reply", reply);
                        replyVO.put("user", userMapper.selectById(reply.getUserId()));
                        // 回复目标
                        User target = reply.getTargetId() == 0 ? null : userMapper.selectById(reply.getTargetId());
                        replyVO.put("target", target);
                        // 点赞数量
                        likeCount = likeService.countLike(Constant.ENTITY_TYPE_COMMENT, reply.getId());
                        replyVO.put("likeCount", likeCount);
                        // 点赞状态
                        likeStatus = loginUser.getUser() == null ? 0 :
                                likeService.likeStatus(loginUser.getUser().getId(), Constant.ENTITY_TYPE_COMMENT, reply.getId());
                        replyVO.put("likeStatus", likeStatus);
                        replyVOList.add(replyVO);
                    }
                    commentVO.put("replys", replyVOList);

                    int replyCount = commentMapper.selectCountByEntity(Constant.ENTITY_TYPE_COMMENT, comment.getId());
                    commentVO.put("replyCount", replyCount);

                    commentVOList.add(commentVO);
                }
            }
        }
        return commentVOList;
    }

    /**
     * 保存评论
     */
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int save(Comment comment) {
        if (comment == null) {
            throw new IllegalArgumentException("参数不能为空!");
        }
        comment.setUserId(loginUser.getUser().getId());
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        comment.setStatus(0);
        comment.setCreateTime(new Date());
        int row = commentMapper.insertComment(comment);
        // 更新文章评论数量
        if (comment.getEntityType() == Constant.ENTITY_TYPE_ARTICLE) {
            int count = commentMapper.selectCountByEntity(comment.getEntityType(), comment.getEntityId());
            articleMapper.updateCommentCount(comment.getEntityId(), count);
        }
        return row;
    }

}
