package com.ssafy.nagne.repository;

import com.ssafy.nagne.domain.Article;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ArticleRepository {

    void save(@Param("article") Article article);

    Optional<Article> findById(@Param("id") Long id);

    List<Article> findAll();
}
