package com.ssafy.nagne.web.follow;

import static com.ssafy.nagne.utils.ApiUtils.success;

import com.ssafy.nagne.security.JwtAuthentication;
import com.ssafy.nagne.service.FollowService;
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
@RequestMapping("api/follow")
@RequiredArgsConstructor
public class FollowRestController {

    private final FollowService followService;

    @PostMapping
    public ApiResult<Boolean> follow(@AuthenticationPrincipal JwtAuthentication authentication,
                                     @Valid @RequestBody FollowRequest request) {
        return success(followService.save(authentication.id(), request.followId()));
    }

    @GetMapping("/{userId}")
    public ApiResult<CheckResult> check(@AuthenticationPrincipal JwtAuthentication authentication,
                                        @PathVariable Long userId) {
        return success(new CheckResult(followService.check(authentication.id(), userId)));
    }

    @DeleteMapping("/{userId}")
    public ApiResult<Boolean> cancel(@AuthenticationPrincipal JwtAuthentication authentication,
                                     @PathVariable Long userId) {
        return success(followService.delete(authentication.id(), userId));
    }
}
