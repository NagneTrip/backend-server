package com.ssafy.nagne.service;

import static lombok.Lombok.checkNotNull;

import com.ssafy.nagne.repository.CommentLikeRepository;
import com.ssafy.nagne.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentLikeService {

    private final CommentLikeRepository commentLikeRepository;
    private final CommentRepository commentRepository;

    public boolean save(Long userId, Long commentId) {
        checkNotNull(userId, "userId must be provided");
        checkNotNull(commentId, "commentId must be provided");

        commentRepository.plusGoodCount(commentId);

        return commentLikeRepository.save(userId, commentId) == 1;
    }

    @Transactional(readOnly = true)
    public boolean check(Long userId, Long commentId) {
        checkNotNull(userId, "userId must be provided");
        checkNotNull(commentId, "commentId must be provided");

        return commentLikeRepository.check(userId, commentId);
    }

    public boolean delete(Long userId, Long commentId) {
        checkNotNull(userId, "userId must be provided");
        checkNotNull(commentId, "commentId must be provided");

        commentRepository.minusGoodCount(commentId);

        return commentLikeRepository.delete(userId, commentId) == 1;
    }
}
