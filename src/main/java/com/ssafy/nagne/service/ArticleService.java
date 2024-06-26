package com.ssafy.nagne.service;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.time.LocalDateTime.now;

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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final ImageRepository imageRepository;

    private final HashTagRepository hashTagRepository;
    private final ArticleHashTagRepository articleHashTagRepository;

    private final FileStore fileStore;

    @Transactional
    public Article save(SaveRequest request, Long sessionId, List<MultipartFile> images) {
        checkArgument(images.size() <= 10, "must be 10 images or less");

        Article newArticle = createNewArticle(request, sessionId);

        return save(newArticle, images);
    }

    public Article findById(Long id, Long sessionId) {
        checkNotNull(id, "id must be provided");

        return articleRepository.findById(id, sessionId)
                .orElseThrow(() -> new NotFoundException("Could not found article for " + id));
    }

    public List<Article> findAll(Pageable pageable) {
        return articleRepository.findAll(pageable);
    }

    public List<Article> findArticlesByUserId(Long userId, Long sessionId, Pageable pageable) {
        checkNotNull(userId, "userId must be provided");

        return articleRepository.findArticlesByUserId(userId, sessionId, pageable);
    }

    public List<Article> findArticlesByTags(List<String> tags, Long sessionId, Pageable pageable) {
        return articleRepository.findArticlesByTags(tags, sessionId, pageable);
    }

    public List<Article> findFollowerArticles(Long sessionId, Pageable pageable) {
        checkNotNull(sessionId, "userId must be provided");

        return articleRepository.findFollowerArticles(sessionId, pageable);
    }

    public List<Article> findBookmarkArticles(Long sessionId, Pageable pageable) {
        checkNotNull(sessionId, "userId must be provided");

        return articleRepository.findBookmarkArticles(sessionId, pageable);
    }

    public List<Article> findTop10Articles(String sort) {
        return articleRepository.findTop10Articles(sort);
    }

    @Transactional
    public boolean update(Long id, Long sessionId, UpdateRequest request) {
        checkNotNull(id, "id must be provided");

        return update(findArticleAndCheckMine(id, sessionId), request);
    }

    @Transactional
    public boolean delete(Long id, Long sessionId) {
        checkNotNull(id, "id must be provided");

        findArticleAndCheckMine(id, sessionId);

        return delete(id);
    }

    private Article createNewArticle(SaveRequest request, Long userId) {
        return Article.builder()
                .userId(userId)
                .content(request.content())
                .createdDate(now())
                .build();
    }

    private Article save(Article newArticle, List<MultipartFile> images) {
        articleRepository.save(newArticle);

        saveImages(newArticle, images);
        saveHashTags(newArticle);

        return newArticle;
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

    private boolean update(Article article, UpdateRequest request) {
        updateContent(article, request);
        updateHashTags(article);

        return articleRepository.update(article) == 1;
    }

    private void updateHashTags(Article article) {
        deleteHashTags(article.getId());
        saveHashTags(article);
    }

    private void updateContent(Article article, UpdateRequest request) {
        article.update(request);
    }

    private boolean delete(Long id) {
        deleteHashTags(id);
        deleteImages(id);

        return articleRepository.delete(id) == 1;
    }

    private Article findArticleAndCheckMine(Long id, Long sessionId) {
        Article article = findById(id, sessionId);

        if (!article.isMine(sessionId)) {
            throw new AccessDeniedException();
        }

        return article;
    }
}
