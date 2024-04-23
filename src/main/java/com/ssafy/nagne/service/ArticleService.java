package com.ssafy.nagne.service;

import static lombok.Lombok.checkNotNull;

import com.ssafy.nagne.domain.Article;
import com.ssafy.nagne.page.PageParameter;
import com.ssafy.nagne.repository.ArticleRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;

    public Article save(Article article) {
        articleRepository.save(article);

        return article;
    }

    public Optional<Article> findById(Long id) {
        checkNotNull(id, "id must be provided");

        Optional<Article> findById = articleRepository.findById(id);
        findById.ifPresent(article -> {
            article.view();
            update(article.getId(), article);
        });

        return findById;
    }

    @Transactional(readOnly = true)
    public List<Article> findAll(PageParameter pageParameter) {
        return articleRepository.findAll(pageParameter);
    }

    @Transactional(readOnly = true)
    public List<Article> findFollowingArticles(Long id, PageParameter pageParameter) {
        checkNotNull(id, "id must be provided");

        return articleRepository.findFollowingArticles(id, pageParameter);
    }

    public boolean update(Long id, Article article) {
        checkNotNull(id, "id must be provided");

        return articleRepository.update(id, article) == 1;
    }

    public boolean delete(Long id) {
        checkNotNull(id, "id must be provided");

        return articleRepository.delete(id) == 1;
    }
}
