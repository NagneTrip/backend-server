package com.ssafy.nagne.web;

import static com.ssafy.nagne.utils.ApiUtils.success;

import com.ssafy.nagne.error.NotFoundException;
import com.ssafy.nagne.security.Jwt;
import com.ssafy.nagne.security.JwtAuthentication;
import com.ssafy.nagne.security.JwtAuthenticationToken;
import com.ssafy.nagne.service.UserService;
import com.ssafy.nagne.utils.ApiUtils.ApiResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserRestController {

    private final Jwt jwt;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    @PostMapping("/login")
    public ApiResult<LoginResult> login(@Valid @RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new JwtAuthenticationToken(request.principal(), request.credentials())
        );

        return success(new LoginResult(jwt, authentication));
    }

    @GetMapping(path = "/me")
    public ApiResult<UserDto> me(@AuthenticationPrincipal JwtAuthentication authentication) {
        return success(
                userService.findById(authentication.id())
                        .map(UserDto::new)
                        .orElseThrow(() -> new NotFoundException("Could not found user for " + authentication.id()))
        );
    }
}
