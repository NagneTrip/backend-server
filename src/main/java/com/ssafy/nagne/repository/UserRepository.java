package com.ssafy.nagne.repository;

import com.ssafy.nagne.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

@Mapper
public interface UserRepository {

    User save(User user);

    Optional<User> findById(@Param("id") Long id);

    Optional<User> findByUsername(@Param("username") String username);

    void update(@Param("user") User user);

    void delete(Long id);
}
