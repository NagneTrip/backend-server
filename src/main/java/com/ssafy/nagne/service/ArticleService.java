package com.ssafy.nagne.service;

import static lombok.Lombok.checkNotNull;

import com.ssafy.nagne.domain.Article;
import com.ssafy.nagne.repository.ArticleRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;

    @Transactional
    public Article save(Article article) {
        articleRepository.save(article);

        return article;
    }

    public Optional<Article> findById(Long id) {
        checkNotNull(id, "id must be provided");

        return articleRepository.findById(id);
    }

    public List<Article> findAll() {
        return articleRepository.findAll();
    }

    @Transactional
    public boolean update(Long id, Article article) {
        checkNotNull(id, "id must be provided");

        return articleRepository.update(id, article) == 1;
    }

    @Transactional
    public boolean delete(Long id) {
        checkNotNull(id, "id must be provided");

        return articleRepository.delete(id) == 1;
    }
}
