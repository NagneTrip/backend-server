package com.ssafy.nagne.web.bookmark;

import com.ssafy.nagne.security.JwtAuthentication;
import com.ssafy.nagne.service.BookmarkService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/bookmark")
@RequiredArgsConstructor
public class BookmarkRestController {

    private final BookmarkService bookmarkService;

    @PostMapping
    public Boolean bookmark(@AuthenticationPrincipal JwtAuthentication authentication,
                            @Valid @RequestBody BookmarkRequest request) {
        return bookmarkService.bookmark(authentication.id(), request.articleId());
    }

    @GetMapping("/{articleId}")
    public CheckResult check(@PathVariable Long articleId, @AuthenticationPrincipal JwtAuthentication authentication) {
        return new CheckResult(bookmarkService.check(authentication.id(), articleId));
    }

    @DeleteMapping("/{articleId}")
    public Boolean cancel(@PathVariable Long articleId, @AuthenticationPrincipal JwtAuthentication authentication) {
        return bookmarkService.cancel(authentication.id(), articleId);
    }
}
