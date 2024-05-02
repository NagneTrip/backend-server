package com.ssafy.nagne.service;

import static lombok.Lombok.checkNotNull;

import com.ssafy.nagne.domain.Article;
import com.ssafy.nagne.error.NotFoundException;
import com.ssafy.nagne.page.PageParameter;
import com.ssafy.nagne.repository.ArticleHashTagRepository;
import com.ssafy.nagne.repository.ArticleRepository;
import com.ssafy.nagne.repository.HashTagRepository;
import com.ssafy.nagne.repository.ImageRepository;
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

    public Article save(Article article, List<MultipartFile> images) {
        articleRepository.save(article);

        saveHashTags(article);

        saveImage(article, images);

        return article;
    }

    public Article findById(Long id) {
        checkNotNull(id, "id must be provided");

        return articleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Could not found article for " + id));
    }

    @Transactional(readOnly = true)
    public List<Article> findAll(List<String> tags, PageParameter pageParameter) {
        return articleRepository.findAll(tags, pageParameter);
    }

    @Transactional(readOnly = true)
    public List<Article> findFollowingArticles(Long userId, PageParameter pageParameter) {
        checkNotNull(userId, "userId must be provided");

        return articleRepository.findFollowingArticles(userId, pageParameter);
    }

    @Transactional(readOnly = true)
    public List<Article> findBookmarkArticles(Long userId, PageParameter pageParameter) {
        checkNotNull(userId, "userId must be provided");

        return articleRepository.findBookmarkArticles(userId, pageParameter);
    }

    public boolean update(Long id, Article article, List<MultipartFile> images) {
        checkNotNull(id, "id must be provided");

        deleteHashTags(id);

        saveHashTags(id, article);

        deleteImage(id);

        saveImage(id, images);

        return articleRepository.update(id, article) == 1;
    }

    public boolean delete(Long id) {
        checkNotNull(id, "id must be provided");

        deleteHashTags(id);

        deleteImage(id);

        return articleRepository.delete(id) == 1;
    }

    private void saveHashTags(Article article) {
        List<String> tags = article.extractHashTags();

        if (!tags.isEmpty()) {
            hashTagRepository.save(tags);
            articleHashTagRepository.save(article.getId(), tags);
        }
    }

    private void saveHashTags(Long articleId, Article article) {
        List<String> tags = article.extractHashTags();

        if (!tags.isEmpty()) {
            hashTagRepository.save(tags);
            articleHashTagRepository.save(articleId, tags);
        }
    }

    private void deleteHashTags(Long id) {
        articleHashTagRepository.delete(id);
    }

    private void saveImage(Article article, List<MultipartFile> images) {
        List<String> filePaths = fileStore.storeFiles(images);

        if (!filePaths.isEmpty()) {
            imageRepository.save(article.getId(), filePaths);
        }
    }

    private void saveImage(Long articleId, List<MultipartFile> images) {
        List<String> filePaths = fileStore.storeFiles(images);

        if (!filePaths.isEmpty()) {
            imageRepository.save(articleId, filePaths);
        }
    }

    private void deleteImage(Long id) {
        imageRepository.delete(id);
    }
}
