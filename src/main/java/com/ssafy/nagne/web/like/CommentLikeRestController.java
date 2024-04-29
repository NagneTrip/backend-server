package com.ssafy.nagne.web.like;

import static com.ssafy.nagne.utils.ApiUtils.success;

import com.ssafy.nagne.security.JwtAuthentication;
import com.ssafy.nagne.service.CommentLikeService;
import com.ssafy.nagne.utils.ApiUtils.ApiResult;
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
    public ApiResult<Boolean> like(@AuthenticationPrincipal JwtAuthentication authentication,
                                   @Valid @RequestBody CommentLikeRequest request) {
        return success(commentLikeService.save(authentication.id(), request.commentId()));
    }

    @GetMapping("/{commentId}")
    public ApiResult<CheckResult> check(@AuthenticationPrincipal JwtAuthentication authentication,
                                        @PathVariable Long commentId) {
        return success(new CheckResult(commentLikeService.check(authentication.id(), commentId)));
    }

    @DeleteMapping("/{commentId}")
    public ApiResult<Boolean> cancel(@AuthenticationPrincipal JwtAuthentication authentication,
                                     @PathVariable Long commentId) {
        return success(commentLikeService.delete(authentication.id(), commentId));
    }
}
