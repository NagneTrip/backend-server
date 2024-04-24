package com.ssafy.nagne.service;

import com.ssafy.nagne.domain.Comment;
import com.ssafy.nagne.repository.CommentRepository;
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
}
