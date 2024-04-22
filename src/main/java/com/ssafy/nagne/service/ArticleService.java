package com.ssafy.nagne.service;

import static lombok.Lombok.checkNotNull;

import com.ssafy.nagne.domain.Article;
import com.ssafy.nagne.repository.ArticleRepository;
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
}
