package com.ssafy.nagne.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CommentLikeRepository {

    int save(@Param("userId") Long userId, @Param("commentId") Long commentId);

    boolean check(@Param("userId") Long userId, @Param("commentId") Long commentId);

    int delete(@Param("userId") Long userId, @Param("commentId") Long commentId);
}
