package com.ssafy.nagne.service;

import static lombok.Lombok.checkNotNull;

import com.ssafy.nagne.domain.User;
import com.ssafy.nagne.error.NotFoundException;
import com.ssafy.nagne.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Transactional
    public User save(User user) {
        user.encodePassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        return user;
    }

    @Transactional
    public User login(String username, String password) {
        User user = findByUsername(username);

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("Bad Credential");
        }

        user.afterLogin();
        userRepository.updateInfo(user.getId(), user);

        return user;
    }

    public User findById(Long id) {
        checkNotNull(id, "id must be provided");

        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("Could not found user for " + id));
    }

    public User findByUsername(String username) {
        checkNotNull(username, "username must be provided");

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Could not found user for " + username));
    }

    public List<User> findAll(String keyword) {
        return userRepository.findAll(keyword);
    }

    public List<User> findFollowers(Long id) {
        checkNotNull(id, "id must be provided");

        return userRepository.findFollowers(id);
    }

    public List<User> findFollowings(Long id) {
        checkNotNull(id, "id must be provided");

        return userRepository.findFollowings(id);
    }

    @Transactional
    public boolean updateInfo(Long id, User user) {
        checkNotNull(id, "id must be provided");

        return userRepository.updateInfo(id, user) == 1;
    }

    @Transactional
    public boolean updateProfileImage(Long id, String path) {
        checkNotNull(id, "id must be provided");

        return userRepository.updateProfileImage(id, path) == 1;
    }

    @Transactional
    public boolean delete(Long id) {
        checkNotNull(id, "id must be provided");

        return userRepository.delete(id) == 1;
    }
}
