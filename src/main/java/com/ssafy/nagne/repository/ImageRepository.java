package com.ssafy.nagne.repository;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ImageRepository {

    void save(@Param("articleId") Long articleId, @Param("filePaths") List<String> FilePaths);
}
