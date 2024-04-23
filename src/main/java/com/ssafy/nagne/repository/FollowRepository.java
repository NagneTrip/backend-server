package com.ssafy.nagne.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface FollowRepository {

    int save(@Param("id") Long id, @Param("followId") Long followId);
}
