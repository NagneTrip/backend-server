package com.ssafy.nagne.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface BookmarkRepository {

    int save(@Param("userId") Long userId, @Param("articleId") Long articleId);

    int countByUserId(@Param("userId") Long userId);

    boolean check(@Param("userId") Long userId, @Param("articleId") Long articleId);

    int delete(@Param("userId") Long userId, @Param("articleId") Long articleId);
}
