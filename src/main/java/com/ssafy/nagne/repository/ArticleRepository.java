package com.ssafy.nagne.repository;

import com.ssafy.nagne.domain.Article;
import com.ssafy.nagne.page.Pageable;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ArticleRepository {

    void save(@Param("article") Article article);

    Optional<Article> findById(@Param("id") Long id);

    List<Article> findArticles(@Param("tags") List<String> tags, @Param("pageable") Pageable pageable);

    List<Article> findFollowerArticles(@Param("userId") Long userId,
                                       @Param("pageable") Pageable pageable);

    List<Article> findBookmarkArticles(@Param("userId") Long userId,
                                       @Param("pageable") Pageable pageable);

    int update(@Param("id") Long id, @Param("article") Article article);

    void plusGoodCount(@Param("id") Long id);

    void minusGoodCount(@Param("id") Long id);

    int delete(@Param("id") Long id);
}
