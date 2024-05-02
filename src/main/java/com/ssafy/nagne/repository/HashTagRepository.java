package com.ssafy.nagne.repository;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface HashTagRepository {

    void save(@Param("tags") List<String> tags);
}
