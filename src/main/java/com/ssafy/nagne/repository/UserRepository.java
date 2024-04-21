package com.ssafy.nagne.repository;

import com.ssafy.nagne.entity.User;
import java.util.Optional;

public interface UserRepository {

    User save(User user);

    Optional<User> findById(Long id);

    Optional<User> findByUsername(String username);

    void update(User user);

    void delete(Long id);
}
