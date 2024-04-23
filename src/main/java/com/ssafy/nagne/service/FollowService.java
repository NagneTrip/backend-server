package com.ssafy.nagne.service;

import static lombok.Lombok.checkNotNull;

import com.ssafy.nagne.repository.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;

    public boolean save(Long id, Long followId) {
        checkNotNull(id, "id must be provided");
        checkNotNull(followId, "id must be provided");

        if (id.equals(followId)) {
            return false;
        }

        return followRepository.save(id, followId) == 1;
    }

    public boolean delete(Long id, Long followId) {
        checkNotNull(id, "id must be provided");
        checkNotNull(followId, "id must be provided");

        if (id.equals(followId)) {
            return false;
        }

        return followRepository.delete(id, followId) == 1;
    }
}
