package com.ssafy.nagne.web.like;

import com.ssafy.nagne.security.JwtAuthentication;
import com.ssafy.nagne.service.ArticleLikeService;
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
@RequestMapping("api/articles/like")
@RequiredArgsConstructor
public class ArticleLikeRestController {

    private final ArticleLikeService articleLikeService;

    @PostMapping
    public Boolean like(@AuthenticationPrincipal JwtAuthentication authentication,
                        @Valid @RequestBody ArticleLikeRequest request) {
        return articleLikeService.like(authentication.id(), request.articleId());
    }

    @GetMapping("/{articleId}")
    public CheckResult check(@PathVariable Long articleId, @AuthenticationPrincipal JwtAuthentication authentication) {
        return new CheckResult(articleLikeService.check(authentication.id(), articleId));
    }

    @DeleteMapping("/{articleId}")
    public Boolean cancel(@PathVariable Long articleId, @AuthenticationPrincipal JwtAuthentication authentication) {
        return articleLikeService.unlike(authentication.id(), articleId);
    }
}
