package com.ssafy.nagne.repository;

import com.ssafy.nagne.domain.Comment;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CommentRepository {

    void save(@Param("comment") Comment comment);

    List<Comment> findCommentsByArticleId(@Param("articleId") Long articleId);

    int update(@Param("id") Long id, @Param("content") String content);

    int delete(@Param("id") Long id);
}
