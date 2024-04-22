package com.ssafy.nagne.service;

import com.ssafy.nagne.domain.Article;
import com.ssafy.nagne.repository.ArticleRepository;
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
}
