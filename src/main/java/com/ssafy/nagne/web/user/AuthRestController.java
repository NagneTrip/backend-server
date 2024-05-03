package com.ssafy.nagne.web.user;

import com.ssafy.nagne.security.Jwt;
import com.ssafy.nagne.security.JwtAuthenticationToken;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class AuthRestController {

    private final Jwt jwt;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public LoginResult login(@Valid @RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new JwtAuthenticationToken(request.username(), request.password())
        );

        return new LoginResult(jwt, authentication);
    }
}
