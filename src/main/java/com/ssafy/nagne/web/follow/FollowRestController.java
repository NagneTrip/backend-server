package com.ssafy.nagne.web.follow;

import com.ssafy.nagne.security.JwtAuthentication;
import com.ssafy.nagne.service.FollowService;
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
    public Boolean follow(@AuthenticationPrincipal JwtAuthentication authentication,
                          @Valid @RequestBody FollowRequest request) {
        return followService.follow(authentication.id(), request.followId());
    }

    @GetMapping("/{userId}")
    public CheckResult check(@AuthenticationPrincipal JwtAuthentication authentication,
                             @PathVariable Long userId) {
        return new CheckResult(followService.check(authentication.id(), userId));
    }

    @DeleteMapping("/{userId}")
    public Boolean unfollow(@AuthenticationPrincipal JwtAuthentication authentication,
                            @PathVariable Long userId) {
        return followService.unfollow(authentication.id(), userId);
    }
}
