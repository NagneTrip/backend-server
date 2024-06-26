package com.ssafy.nagne.service;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.ssafy.nagne.domain.Tier.UNRANKED;
import static java.time.LocalDateTime.now;

import com.ssafy.nagne.domain.Gender;
import com.ssafy.nagne.domain.User;
import com.ssafy.nagne.error.AccessDeniedException;
import com.ssafy.nagne.error.NotFoundException;
import com.ssafy.nagne.page.Pageable;
import com.ssafy.nagne.repository.UserRepository;
import com.ssafy.nagne.web.user.JoinRequest;
import com.ssafy.nagne.web.user.UpdateRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    private final FileStore fileStore;

    @Transactional
    public User save(JoinRequest request) {
        User newUser = createNewUser(request);

        userRepository.save(newUser);

        return newUser;
    }

    private User createNewUser(JoinRequest request) {
        return User.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .nickname(request.nickname())
                .followers(0)
                .followings(0)
                .phone(request.phone())
                .birth(request.birth())
                .gender(Gender.of(request.gender()))
                .tier(UNRANKED)
                .createdDate(now())
                .lastModifiedDate(now())
                .build();
    }

    @Transactional
    public User loginOAuth(String username) {
        checkNotNull(username, "username must be provided");

        User user = findByUsername(username);

        updateLastLoginDate(user);

        return user;
    }

    @Transactional
    public User login(String username, String password) {
        checkNotNull(username, "username must be provided");
        checkNotNull(password, "password must be provided");

        User user = findByUsername(username);
        user.checkPassword(passwordEncoder, password);

        updateLastLoginDate(user);

        return user;
    }

    private void updateLastLoginDate(User user) {
        user.afterLogin();
        userRepository.update(user);
    }

    public User findById(Long id) {
        checkNotNull(id, "id must be provided");

        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("Could not found user for " + id));
    }

    public User findById(Long sessionId, Long id) {
        checkNotNull(id, "id must be provided");

        return findUserAndCheckMe(id, sessionId);
    }

    public User findByUsername(String username) {
        checkNotNull(username, "username must be provided");

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Could not found user for " + username));
    }

    public List<User> findUsersByKeyword(String keyword, Long sessionId, Pageable pageable) {
        return userRepository.findUsersByKeyword(keyword, sessionId, pageable);
    }

    public List<User> findFollowers(Long id, Long sessionId, Pageable pageable) {
        checkNotNull(id, "id must be provided");

        return userRepository.findFollowers(id, sessionId, pageable);
    }

    public List<User> findFollowings(Long id, Long sessionId, Pageable pageable) {
        checkNotNull(id, "id must be provided");

        return userRepository.findFollowings(id, sessionId, pageable);
    }

    @Transactional
    public boolean update(Long id, Long sessionId, UpdateRequest request, MultipartFile profileImage) {
        checkNotNull(id, "id must be provided");

        User user = findUserAndCheckMe(id, sessionId);

        user.updateInfo(request, fileStore.store(profileImage));

        return userRepository.update(user) == 1;
    }

    @Transactional
    public boolean delete(Long id, Long sessionId) {
        checkNotNull(id, "id must be provided");

        findUserAndCheckMe(id, sessionId);

        return userRepository.delete(id) == 1;
    }

    private User findUserAndCheckMe(Long id, Long sessionId) {
        User user = findById(id);

        if (!user.isMe(sessionId)) {
            throw new AccessDeniedException();
        }

        return user;
    }
}
