package com.ssafy.nagne.service;

import static lombok.Lombok.checkNotNull;

import com.ssafy.nagne.repository.ArticleRepository;
import com.ssafy.nagne.repository.LikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final ArticleRepository articleRepository;

    public boolean save(Long userId, Long articleId) {
        checkNotNull(userId, "userId must be provided");
        checkNotNull(articleId, "articleId must be provided");

        articleRepository.plusGoodCount(articleId);

        return likeRepository.save(userId, articleId) == 1;
    }

    @Transactional(readOnly = true)
    public boolean check(Long userId, Long articleId) {
        checkNotNull(userId, "userId must be provided");
        checkNotNull(articleId, "articleId must be provided");

        return likeRepository.check(userId, articleId);
    }

    public boolean delete(Long userId, Long articleId) {
        checkNotNull(userId, "userId must be provided");
        checkNotNull(articleId, "articleId must be provided");

        articleRepository.minusGoodCount(articleId);

        return likeRepository.delete(userId, articleId) == 1;
    }
}
