package com.ssafy.nagne.web.user;

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
    public JoinResult join(@Valid @RequestPart JoinRequest request, @RequestPart MultipartFile profileImage) {
        return new JoinResult(userService.save(request, profileImage));
    }

    @GetMapping("/{id}")
    public UserResult findById(@PathVariable Long id) {
        return new UserResult(userService.findById(id));
    }

    @GetMapping("/{id}/detail")
    public UserDetailResult findByIdDetail(@AuthenticationPrincipal JwtAuthentication authentication,
                                           @PathVariable Long id) {
        return new UserDetailResult(userService.findById(authentication.id(), id));
    }

    @GetMapping
    public UserListResult findUsers(@RequestParam(required = false) String keyword) {
        return new UserListResult(userService.findUsers(keyword));
    }

    @GetMapping("/{id}/followers")
    public UserListResult findFollowers(@PathVariable Long id) {
        return new UserListResult(userService.findFollowers(id));
    }

    @GetMapping("/{id}/followings")
    public UserListResult findFollowings(@PathVariable Long id) {
        return new UserListResult(userService.findFollowings(id));
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
