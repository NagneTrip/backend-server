package com.ssafy.nagne.service;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.time.LocalDateTime.now;

import com.ssafy.nagne.domain.Comment;
import com.ssafy.nagne.error.NotFoundException;
import com.ssafy.nagne.repository.CommentRepository;
import com.ssafy.nagne.web.comment.SaveRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    public Comment save(SaveRequest request, Long userId) {
        Comment newComment = createNewComment(request, userId);

        commentRepository.save(newComment);

        return newComment;
    }

    public Comment findById(Long id) {
        checkNotNull(id, "id must be provided");

        return commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Could not found comment for " + id));
    }

    public List<Comment> findCommentsByArticleId(Long articleId) {
        checkNotNull(articleId, "articleId must be provided");

        return commentRepository.findCommentsByArticleId(articleId);
    }

    public boolean update(Long id, String content) {
        checkNotNull(id, "id must be provided");

        return commentRepository.update(id, content) == 1;
    }

    public boolean delete(Long id) {
        checkNotNull(id, "id must be provided");

        return commentRepository.delete(id) == 1;
    }

    private Comment createNewComment(SaveRequest request, Long userId) {
        return Comment.builder()
                .articleId(request.articleId())
                .userId(userId)
                .content(request.content())
                .createdDate(now())
                .build();
    }
}
