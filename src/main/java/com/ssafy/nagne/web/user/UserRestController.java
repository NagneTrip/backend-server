package com.ssafy.nagne.web.user;

import com.ssafy.nagne.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserRestController {

    private final UserService userService;

    @GetMapping("/{id}")
    public UserDetailResult findById(@PathVariable Long id) {
        return new UserDetailResult(userService.findById(id));
    }

    @GetMapping
    public UserListResult findAll(@RequestParam(required = false) String keyword) {
        return new UserListResult(userService.findAll(keyword));
    }

    @GetMapping("/{id}/followers")
    public UserListResult findFollowers(@PathVariable Long id) {
        return new UserListResult(userService.findFollowers(id));
    }

    @GetMapping("/{id}/followings")
    public UserListResult findFollowings(@PathVariable Long id) {
        return new UserListResult(userService.findFollowings(id));
    }
}
