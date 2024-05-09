package com.ssafy.nagne.service;

import static com.google.common.base.Preconditions.checkNotNull;

import com.ssafy.nagne.domain.Article;
import com.ssafy.nagne.error.DuplicateException;
import com.ssafy.nagne.error.NotFoundException;
import com.ssafy.nagne.repository.ArticleLikeRepository;
import com.ssafy.nagne.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ArticleLikeService {

    private final ArticleLikeRepository articleLikeRepository;
    private final ArticleRepository articleRepository;

    public boolean like(Long userId, Long articleId) {
        checkNotNull(userId, "userId must be provided");
        checkNotNull(articleId, "articleId must be provided");

        return save(userId, articleId);
    }

    @Transactional(readOnly = true)
    public boolean check(Long userId, Long articleId) {
        checkNotNull(userId, "userId must be provided");
        checkNotNull(articleId, "articleId must be provided");

        return articleLikeRepository.check(userId, articleId);
    }

    public boolean unlike(Long userId, Long articleId) {
        checkNotNull(userId, "userId must be provided");
        checkNotNull(articleId, "articleId must be provided");

        return delete(userId, articleId);
    }

    private boolean save(Long userId, Long articleId) {
        Article article = findArticle(articleId);

        like(article);

        try {
            return articleLikeRepository.save(userId, articleId) == 1;
        } catch (DuplicateKeyException e) {
            throw new DuplicateException("already liked");
        }
    }

    private void like(Article article) {
        article.like();
        articleRepository.update(article);
    }

    private boolean delete(Long userId, Long articleId) {
        if (!check(userId, articleId)) {
            throw new DuplicateException("already unliked");
        }

        Article article = findArticle(articleId);
        unlike(article);

        return articleLikeRepository.delete(userId, articleId) == 1;
    }

    private void unlike(Article article) {
        article.unlike();
        articleRepository.update(article);
    }

    private Article findArticle(Long articleId) {
        return articleRepository.findById(articleId)
                .orElseThrow(() -> new NotFoundException("Could not found article for " + articleId));
    }
}
