package com.ssafy.nagne.service;

import static lombok.Lombok.checkNotNull;

import com.ssafy.nagne.domain.Article;
import com.ssafy.nagne.error.NotFoundException;
import com.ssafy.nagne.page.PageParameter;
import com.ssafy.nagne.repository.ArticleRepository;
import com.ssafy.nagne.repository.ImageRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final ImageRepository imageRepository;
    private final FileStore fileStore;

    public Article save(Article article, List<MultipartFile> images) {
        articleRepository.save(article);

        saveImage(article, images);

        return article;
    }

    public Article findById(Long id) {
        checkNotNull(id, "id must be provided");

        return articleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Could not found article for " + id));
    }

    @Transactional(readOnly = true)
    public List<Article> findAll(PageParameter pageParameter) {
        return articleRepository.findAll(pageParameter);
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

    public boolean update(Long id, Article article) {
        checkNotNull(id, "id must be provided");

        return articleRepository.update(id, article) == 1;
    }

    public boolean delete(Long id) {
        checkNotNull(id, "id must be provided");

        return articleRepository.delete(id) == 1;
    }

    private void saveImage(Article article, List<MultipartFile> images) {
        List<String> filePaths = fileStore.storeFiles(images);

        if (!filePaths.isEmpty()) {
            imageRepository.save(article.getId(), filePaths);
        }
    }
}
