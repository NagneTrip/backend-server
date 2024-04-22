package com.ssafy.nagne.repository;

import com.ssafy.nagne.domain.User;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserRepository {

    void save(@Param("user") User user);

    Optional<User> findById(@Param("id") Long id);

    Optional<User> findByUsername(@Param("username") String username);

    int updateInfo(@Param("id") Long id, @Param("user") User user);

    int updateProfileImage(@Param("id") Long id, @Param("path") String path);

    int delete(@Param("id") Long id);
}
