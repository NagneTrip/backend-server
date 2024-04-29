package com.ssafy.nagne.service;

import static lombok.Lombok.checkNotNull;

import com.ssafy.nagne.repository.ArticleLikeRepository;
import com.ssafy.nagne.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ArticleLikeService {

    private final ArticleLikeRepository articleLikeRepository;
    private final ArticleRepository articleRepository;

    public boolean save(Long userId, Long articleId) {
        checkNotNull(userId, "userId must be provided");
        checkNotNull(articleId, "articleId must be provided");

        articleRepository.plusGoodCount(articleId);

        return articleLikeRepository.save(userId, articleId) == 1;
    }

    @Transactional(readOnly = true)
    public boolean check(Long userId, Long articleId) {
        checkNotNull(userId, "userId must be provided");
        checkNotNull(articleId, "articleId must be provided");

        return articleLikeRepository.check(userId, articleId);
    }

    public boolean delete(Long userId, Long articleId) {
        checkNotNull(userId, "userId must be provided");
        checkNotNull(articleId, "articleId must be provided");

        if (articleLikeRepository.delete(userId, articleId) != 1) {
            return false;
        }

        articleRepository.minusGoodCount(articleId);
        return true;
    }
}
