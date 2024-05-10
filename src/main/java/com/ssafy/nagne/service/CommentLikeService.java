package com.ssafy.nagne.service;

import static com.google.common.base.Preconditions.checkNotNull;

import com.ssafy.nagne.error.DuplicateException;
import com.ssafy.nagne.error.NotFoundException;
import com.ssafy.nagne.repository.CommentLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentLikeService {

    private final CommentLikeRepository commentLikeRepository;

    public boolean like(Long userId, Long commentId) {
        checkNotNull(userId, "userId must be provided");
        checkNotNull(commentId, "commentId must be provided");

        return save(userId, commentId);
    }

    @Transactional(readOnly = true)
    public boolean check(Long userId, Long commentId) {
        checkNotNull(userId, "userId must be provided");
        checkNotNull(commentId, "commentId must be provided");

        return commentLikeRepository.check(userId, commentId);
    }

    public boolean unlike(Long userId, Long commentId) {
        checkNotNull(userId, "userId must be provided");
        checkNotNull(commentId, "commentId must be provided");

        return delete(userId, commentId);
    }

    private boolean save(Long userId, Long commentId) {
        try {
            return commentLikeRepository.save(userId, commentId) == 1;
        } catch (DuplicateKeyException e) {
            throw new DuplicateException("already liked");
        } catch (DataIntegrityViolationException e) {
            throw new NotFoundException("Could not found comment for " + commentId);
        }
    }

    private boolean delete(Long userId, Long commentId) {
        if (!check(userId, commentId)) {
            throw new DuplicateException("already unliked");
        }

        return commentLikeRepository.delete(userId, commentId) == 1;
    }
}
