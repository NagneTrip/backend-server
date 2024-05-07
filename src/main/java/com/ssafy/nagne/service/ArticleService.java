package com.ssafy.nagne.service;

import static java.time.LocalDateTime.now;
import static lombok.Lombok.checkNotNull;

import com.ssafy.nagne.domain.Article;
import com.ssafy.nagne.error.AccessDeniedException;
import com.ssafy.nagne.error.NotFoundException;
import com.ssafy.nagne.page.Pageable;
import com.ssafy.nagne.repository.ArticleHashTagRepository;
import com.ssafy.nagne.repository.ArticleRepository;
import com.ssafy.nagne.repository.HashTagRepository;
import com.ssafy.nagne.repository.ImageRepository;
import com.ssafy.nagne.web.article.SaveRequest;
import com.ssafy.nagne.web.article.UpdateRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final ImageRepository imageRepository;

    private final HashTagRepository hashTagRepository;
    private final ArticleHashTagRepository articleHashTagRepository;

    private final FileStore fileStore;

    public Article save(SaveRequest request, Long userId, List<MultipartFile> images) {
        Article newArticle = createNewArticle(request, userId);

        save(newArticle);

        saveImages(newArticle, images);

        saveHashTags(newArticle);

        return newArticle;
    }

    private Article createNewArticle(SaveRequest request, Long userId) {
        return Article.builder()
                .userId(userId)
                .content(request.content())
                .createdDate(now())
                .build();
    }

    public Article findById(Long id) {
        checkNotNull(id, "id must be provided");

        return articleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Could not found article for " + id));
    }

    @Transactional(readOnly = true)
    public List<Article> findArticles(List<String> tags, Pageable pageable) {
        return articleRepository.findArticles(tags, pageable);
    }

    @Transactional(readOnly = true)
    public List<Article> findFollowerArticles(Long userId, Pageable pageable) {
        checkNotNull(userId, "userId must be provided");

        return articleRepository.findFollowerArticles(userId, pageable);
    }

    @Transactional(readOnly = true)
    public List<Article> findBookmarkArticles(Long userId, Pageable pageable) {
        checkNotNull(userId, "userId must be provided");

        return articleRepository.findBookmarkArticles(userId, pageable);
    }

    public boolean update(Long id, Long sessionId, UpdateRequest request, List<MultipartFile> images) {
        checkNotNull(id, "id must be provided");

        Article article = findAndCheckArticle(id, sessionId);

        updateHashTags(article);

        updateImages(article, images);

        updateContent(article, request);

        return articleRepository.update(id, article) == 1;
    }

    private void updateHashTags(Article article) {
        deleteHashTags(article.getId());
        saveHashTags(article);
    }

    private void updateImages(Article article, List<MultipartFile> images) {
        deleteImages(article.getId());
        saveImages(article, images);
    }

    private void updateContent(Article article, UpdateRequest request) {
        article.update(request);
    }

    public boolean delete(Long id) {
        checkNotNull(id, "id must be provided");

        deleteHashTags(id);

        deleteImages(id);

        return articleRepository.delete(id) == 1;
    }

    private Article findAndCheckArticle(Long id, Long sessionId) {
        Article article = findById(id);

        if (!article.isMine(sessionId)) {
            throw new AccessDeniedException();
        }

        return article;
    }

    private void save(Article newArticle) {
        articleRepository.save(newArticle);
    }

    private void saveHashTags(Article article) {
        List<String> tags = article.extractHashTags();

        if (!tags.isEmpty()) {
            hashTagRepository.save(tags);
            articleHashTagRepository.save(article.getId(), tags);
        }
    }

    private void deleteHashTags(Long id) {
        articleHashTagRepository.delete(id);
    }

    private void saveImages(Article article, List<MultipartFile> images) {
        List<String> filePaths = fileStore.store(images);

        if (!filePaths.isEmpty()) {
            imageRepository.save(article.getId(), filePaths);
        }

        article.update(filePaths);
    }

    private void deleteImages(Long id) {
        imageRepository.delete(id);
    }
}
