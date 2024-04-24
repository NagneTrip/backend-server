package com.ssafy.nagne.service;

import static lombok.Lombok.checkNotNull;

import com.ssafy.nagne.domain.Comment;
import com.ssafy.nagne.repository.CommentRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    public Comment save(Comment comment) {
        commentRepository.save(comment);

        return comment;
    }

    public List<Comment> findCommentsByArticleId(Long articleId) {
        checkNotNull(articleId, "articleId must be provided");

        return commentRepository.findCommentsByArticleId(articleId);
    }

    public boolean update(Long id, String content) {
        checkNotNull(id, "id must be provided");

        return commentRepository.update(id, content) == 1;
    }
}
