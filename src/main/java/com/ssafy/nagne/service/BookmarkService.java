package com.ssafy.nagne.service;

import static com.google.common.base.Preconditions.checkNotNull;

import com.ssafy.nagne.error.DuplicateException;
import com.ssafy.nagne.error.NotFoundException;
import com.ssafy.nagne.repository.BookmarkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;

    public boolean bookmark(Long userId, Long articleId) {
        checkNotNull(userId, "userId must be provided");
        checkNotNull(articleId, "articleId must be provided");

        return save(userId, articleId);
    }

    @Transactional(readOnly = true)
    public int countByUserId(Long userId) {
        checkNotNull(userId, "userId must be provided");

        return bookmarkRepository.countByUserId(userId);
    }

    @Transactional(readOnly = true)
    public boolean check(Long userId, Long articleId) {
        checkNotNull(userId, "userId must be provided");
        checkNotNull(articleId, "articleId must be provided");

        return bookmarkRepository.check(userId, articleId);
    }

    public boolean cancel(Long userId, Long articleId) {
        checkNotNull(userId, "userId must be provided");
        checkNotNull(articleId, "articleId must be provided");

        return delete(userId, articleId);
    }

    private boolean save(Long userId, Long articleId) {
        try {
            return bookmarkRepository.save(userId, articleId) == 1;
        } catch (DuplicateKeyException e) {
            throw new DuplicateException("already bookmarked");
        } catch (DataIntegrityViolationException e) {
            throw new NotFoundException("Could not found article for " + articleId);
        }
    }

    private boolean delete(Long userId, Long articleId) {
        if (!check(userId, articleId)) {
            throw new DuplicateException("already canceled");
        }

        return bookmarkRepository.delete(userId, articleId) == 1;
    }
}
