package com.ssafy.nagne.service;

import static lombok.Lombok.checkNotNull;

import com.ssafy.nagne.domain.User;
import com.ssafy.nagne.error.NotFoundException;
import com.ssafy.nagne.repository.UserRepository;
import java.util.Optional;
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
        User user = findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Could not fount user for " + username));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("Bad Credential");
        }

        user.afterLogin();
        userRepository.update(user.getId(), user);

        return user;
    }

    public Optional<User> findById(Long id) {
        checkNotNull(id, "id must be provided");

        return userRepository.findById(id);
    }

    public Optional<User> findByUsername(String username) {
        checkNotNull(username, "username must be provided");

        return userRepository.findByUsername(username);
    }

    @Transactional
    public boolean updateInfo(Long id, User user) {
        return userRepository.update(id, user) == 1;
    }

    @Transactional
    public boolean delete(Long id) {
        return userRepository.delete(id) == 1;
    }
}
