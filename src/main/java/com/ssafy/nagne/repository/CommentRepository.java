package com.ssafy.nagne.repository;

import com.ssafy.nagne.domain.Comment;
import com.ssafy.nagne.page.Pageable;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CommentRepository {

    void save(@Param("comment") Comment comment);

    Optional<Comment> findById(@Param("id") Long id,
                               @Param("userId") Long userId);

    List<Comment> findCommentsByArticleId(@Param("articleId") Long articleId,
                                          @Param("userId") Long userId,
                                          @Param("pageable") Pageable pageable);

    int update(@Param("comment") Comment comment);

    int delete(@Param("id") Long id);
}
