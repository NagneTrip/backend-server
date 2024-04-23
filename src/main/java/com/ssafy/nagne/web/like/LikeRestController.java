package com.ssafy.nagne.web.like;

import static com.ssafy.nagne.utils.ApiUtils.success;

import com.ssafy.nagne.security.JwtAuthentication;
import com.ssafy.nagne.service.LikeService;
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
@RequestMapping("api/like")
@RequiredArgsConstructor
public class LikeRestController {

    private final LikeService likeService;

    @PostMapping
    public ApiResult<Boolean> like(@AuthenticationPrincipal JwtAuthentication authentication,
                                   @Valid @RequestBody LikeRequest request) {
        return success(likeService.save(authentication.id(), request.articleId()));
    }

    @GetMapping("/{articleId}")
    public ApiResult<CheckResult> check(@AuthenticationPrincipal JwtAuthentication authentication,
                                        @PathVariable Long articleId) {
        return success(new CheckResult(likeService.check(authentication.id(), articleId)));
    }

    @DeleteMapping("/{articleId}")
    public ApiResult<Boolean> cancel(@AuthenticationPrincipal JwtAuthentication authentication,
                                     @PathVariable Long articleId) {
        return success(likeService.delete(authentication.id(), articleId));
    }
}
