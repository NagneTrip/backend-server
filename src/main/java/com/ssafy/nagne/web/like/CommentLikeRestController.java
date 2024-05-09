package com.ssafy.nagne.web.like;

import com.ssafy.nagne.security.JwtAuthentication;
import com.ssafy.nagne.service.CommentLikeService;
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
@RequestMapping("api/comments/like")
@RequiredArgsConstructor
public class CommentLikeRestController {

    private final CommentLikeService commentLikeService;

    @PostMapping
    public Boolean like(@AuthenticationPrincipal JwtAuthentication authentication,
                        @Valid @RequestBody CommentLikeRequest request) {
        return commentLikeService.like(authentication.id(), request.commentId());
    }

    @GetMapping("/{commentId}")
    public CheckResult check(@PathVariable Long commentId, @AuthenticationPrincipal JwtAuthentication authentication) {
        return new CheckResult(commentLikeService.check(authentication.id(), commentId));
    }

    @DeleteMapping("/{commentId}")
    public Boolean unlike(@PathVariable Long commentId, @AuthenticationPrincipal JwtAuthentication authentication) {
        return commentLikeService.unlike(authentication.id(), commentId);
    }
}
