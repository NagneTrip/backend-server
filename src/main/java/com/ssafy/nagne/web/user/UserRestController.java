package com.ssafy.nagne.web.user;

import static com.ssafy.nagne.utils.ApiUtils.success;

import com.ssafy.nagne.error.NotFoundException;
import com.ssafy.nagne.service.UserService;
import com.ssafy.nagne.utils.ApiUtils.ApiResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserRestController {

    private final UserService userService;

    @GetMapping("/{id}")
    public ApiResult<UserDetailResult> findById(@PathVariable Long id) {
        return success(
                new UserDetailResult(
                        userService.findById(id)
                                .orElseThrow(() -> new NotFoundException(
                                        "Could not found user for " + id))
                )
        );
    }
}
