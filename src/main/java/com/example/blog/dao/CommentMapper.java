package com.example.blog.dao;

import com.example.blog.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface CommentMapper {

    int selectCommentCountByUserId(@Param("userId")int userId);

    Comment selectCommentById(@Param("id")int id);

    List<Comment> selectCommentsByUserId(@Param("id")int id,@Param("offset")int offset,@Param("limit")int limit);

    List<Comment> selectReplyListByIds(@Param("ids")List<Integer> ids);

    List<Comment> selectCommentsByEntity(@Param("entityType")int entityType, @Param("entityId")int entityId, @Param("offset")int offset, @Param("limit")int limit);

    int selectCountByEntity(@Param("entityType")int entityType, @Param("entityId")int entityId);

    int selectCount();

    List<Comment> selectCountsByEntity(@Param("entityType")int entityType, @Param("ids")List<Integer> entityIds);

    int insertComment(Comment comment);

    int updateCommentByArticleId(@Param("entityId")int entityId, @Param("status")int status,@Param("updateTime") Date updateTime);

    int updateReplyByArticleId(@Param("entityId")int entityId, @Param("status")int status,@Param("updateTime") Date updateTime);

    int updateComment(@Param("id")int id,  @Param("status")int status,@Param("updateTime") Date updateTime);

    int updateReplyByCommentId(@Param("id")int id, @Param("status")int status,@Param("updateTime") Date updateTime);
}
