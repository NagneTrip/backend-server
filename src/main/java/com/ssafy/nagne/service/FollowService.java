package com.ssafy.nagne.service;

import static com.google.common.base.Preconditions.checkNotNull;

import com.ssafy.nagne.error.DuplicateException;
import com.ssafy.nagne.error.NotFoundException;
import com.ssafy.nagne.repository.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;

    public boolean follow(Long id, Long followId) {
        checkNotNull(id, "id must be provided");
        checkNotNull(followId, "id must be provided");

        if (id.equals(followId)) {
            return false;
        }

        return save(id, followId);
    }

    @Transactional(readOnly = true)
    public boolean check(Long id, Long followId) {
        checkNotNull(id, "id must be provided");
        checkNotNull(followId, "id must be provided");

        return followRepository.check(id, followId);
    }

    public boolean unfollow(Long id, Long followId) {
        checkNotNull(id, "id must be provided");
        checkNotNull(followId, "id must be provided");

        if (id.equals(followId)) {
            return false;
        }

        return delete(id, followId);
    }

    private boolean save(Long id, Long followId) {
        try {
            return followRepository.save(id, followId) == 1;
        } catch (DuplicateKeyException e) {
            throw new DuplicateException("already followed");
        } catch (DataIntegrityViolationException e) {
            throw new NotFoundException("Could not found user for " + followId);
        }
    }

    private boolean delete(Long id, Long followId) {
        if (!check(id, followId)) {
            throw new DuplicateException("already unfollowed");
        }

        return followRepository.delete(id, followId) == 1;
    }
}
