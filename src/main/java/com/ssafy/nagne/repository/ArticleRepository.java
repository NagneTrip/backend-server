package com.ssafy.nagne.repository;

import com.ssafy.nagne.domain.Article;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ArticleRepository {

    void save(@Param("article") Article article);
}
