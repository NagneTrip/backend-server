package com.ssafy.nagne.web.user;

import com.ssafy.nagne.page.Pageable;
import com.ssafy.nagne.security.JwtAuthentication;
import com.ssafy.nagne.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserRestController {

    private final UserService userService;

    @PostMapping
    public JoinResult join(@Valid @RequestBody JoinRequest request) {
        return new JoinResult(userService.save(request));
    }

    @GetMapping("/{id}")
    public UserResult findById(@PathVariable Long id) {
        return new UserResult(userService.findById(id));
    }

    @GetMapping("/{id}/detail")
    public UserDetailResult findByIdDetail(@PathVariable Long id,
                                           @AuthenticationPrincipal JwtAuthentication authentication) {
        return new UserDetailResult(userService.findById(authentication.id(), id));
    }

    @GetMapping
    public UserListResult findUsersByKeyword(@RequestParam(required = false) String keyword,
                                             @AuthenticationPrincipal JwtAuthentication authentication,
                                             Pageable pageable) {
        return new UserListResult(userService.findUsersByKeyword(keyword, authentication.id(), pageable));
    }

    @GetMapping("/{id}/followers")
    public UserListResult findFollowers(@PathVariable Long id,
                                        @AuthenticationPrincipal JwtAuthentication authentication,
                                        Pageable pageable) {
        return new UserListResult(userService.findFollowers(id, authentication.id(), pageable));
    }

    @GetMapping("/{id}/followings")
    public UserListResult findFollowings(@PathVariable Long id,
                                         @AuthenticationPrincipal JwtAuthentication authentication,
                                         Pageable pageable) {
        return new UserListResult(userService.findFollowings(id, authentication.id(), pageable));
    }

    @PatchMapping("/{id}")
    public Boolean update(@PathVariable Long id, @AuthenticationPrincipal JwtAuthentication authentication,
                          @RequestPart UpdateRequest request,
                          @RequestPart MultipartFile profileImage) {
        return userService.update(id, authentication.id(), request, profileImage);
    }

    @DeleteMapping("/{id}")
    public Boolean delete(@PathVariable Long id, @AuthenticationPrincipal JwtAuthentication authentication) {
        return userService.delete(id, authentication.id());
    }
}
