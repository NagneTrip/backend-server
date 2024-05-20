package com.ssafy.nagne.repository;

import com.ssafy.nagne.domain.User;
import com.ssafy.nagne.page.Pageable;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserRepository {

    void save(@Param("user") User user);

    Optional<User> findById(@Param("id") Long id);

    Optional<User> findByUsername(@Param("username") String username);

    List<User> findUsersByKeyword(@Param("keyword") String keyword,
                                  @Param("sessionId") Long sessionId,
                                  @Param("pageable") Pageable pageable);

    List<User> findFollowers(@Param("id") Long id,
                             @Param("sessionId") Long sessionId,
                             @Param("pageable") Pageable pageable);

    List<User> findFollowings(@Param("id") Long id,
                              @Param("sessionId") Long sessionId,
                              @Param("pageable") Pageable pageable);

    int update(@Param("user") User user);

    int delete(@Param("id") Long id);
}
