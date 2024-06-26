package com.ssafy.nagne.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface FollowRepository {

    int save(@Param("id") Long id, @Param("followId") Long followId);

    boolean check(@Param("id") Long id, @Param("followId") Long followId);

    int delete(@Param("id") Long id, @Param("followId") Long followId);
}
