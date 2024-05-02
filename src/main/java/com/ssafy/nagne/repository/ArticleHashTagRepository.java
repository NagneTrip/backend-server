package com.ssafy.nagne.repository;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ArticleHashTagRepository {

    void save(@Param("articleId") Long articleId, @Param("tags") List<String> tags);

    int delete(@Param("articleId") Long articleId);
}
