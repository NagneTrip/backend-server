package com.ssafy.nagne.service;

import static lombok.Lombok.checkNotNull;

import com.ssafy.nagne.domain.Comment;
import com.ssafy.nagne.error.DuplicateException;
import com.ssafy.nagne.error.NotFoundException;
import com.ssafy.nagne.repository.CommentLikeRepository;
import com.ssafy.nagne.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentLikeService {

    private final CommentLikeRepository commentLikeRepository;
    private final CommentRepository commentRepository;

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
        Comment comment = findComment(commentId);

        like(comment);

        try {
            return commentLikeRepository.save(userId, commentId) == 1;
        } catch (DuplicateKeyException e) {
            throw new DuplicateException("already liked");
        }
    }

    private void like(Comment comment) {
        comment.like();
        commentRepository.update(comment);
    }

    private boolean delete(Long userId, Long commentId) {
        Comment comment = findComment(commentId);

        if (!check(userId, commentId)) {
            throw new DuplicateException("already unliked");
        }

        unlike(comment);

        return commentLikeRepository.delete(userId, commentId) == 1;
    }

    private void unlike(Comment comment) {
        comment.unlike();
        commentRepository.update(comment);
    }

    private Comment findComment(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Could not found comment for " + commentId));
    }
}
