package com.ssafy.nagne.web.user;

import static com.ssafy.nagne.utils.ApiUtils.success;

import com.ssafy.nagne.security.Jwt;
import com.ssafy.nagne.security.JwtAuthenticationToken;
import com.ssafy.nagne.utils.ApiUtils.ApiResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/user")
@RequiredArgsConstructor
public class AuthRestController {

    private final Jwt jwt;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ApiResult<LoginResult> login(@Valid @RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new JwtAuthenticationToken(request.principal(), request.credentials())
        );

        return success(
                new LoginResult(jwt, authentication)
        );
    }
}
