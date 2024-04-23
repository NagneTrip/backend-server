package com.ssafy.nagne.service;

import com.ssafy.nagne.repository.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;

    public boolean save(Long id, Long followId) {
        if (id.equals(followId)) {
            return false;
        }

        return followRepository.save(id, followId) == 1;
    }
}
