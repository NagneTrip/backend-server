package com.ssafy.nagne.service;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.time.LocalDateTime.now;

import com.ssafy.nagne.domain.Comment;
import com.ssafy.nagne.error.AccessDeniedException;
import com.ssafy.nagne.error.NotFoundException;
import com.ssafy.nagne.page.Pageable;
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

    @Transactional(readOnly = true)
    public Comment findById(Long id, Long sessionId) {
        checkNotNull(id, "id must be provided");

        return commentRepository.findById(id, sessionId)
                .orElseThrow(() -> new NotFoundException("Could not found comment for " + id));
    }

    @Transactional(readOnly = true)
    public List<Comment> findCommentsByArticleId(Long articleId, Long sessionId, Pageable pageable) {
        checkNotNull(articleId, "articleId must be provided");

        return commentRepository.findCommentsByArticleId(articleId, sessionId, pageable);
    }

    public boolean update(Long id, Long sessionId, String content) {
        checkNotNull(id, "id must be provided");

        return update(findCommentAndCheckMine(id, sessionId), content);
    }

    public boolean delete(Long id, Long sessionId) {
        checkNotNull(id, "id must be provided");

        findCommentAndCheckMine(id, sessionId);

        return delete(id);
    }

    private Comment createNewComment(SaveRequest request, Long userId) {
        return Comment.builder()
                .articleId(request.articleId())
                .userId(userId)
                .content(request.content())
                .createdDate(now())
                .build();
    }

    private Comment findCommentAndCheckMine(Long id, Long sessionId) {
        Comment comment = findById(id, sessionId);

        if (!comment.isMine(sessionId)) {
            throw new AccessDeniedException();
        }

        return comment;
    }

    private boolean update(Comment comment, String content) {
        comment.update(content);

        return commentRepository.update(comment) == 1;
    }

    private boolean delete(Long id) {
        return commentRepository.delete(id) == 1;
    }
}
