package com.ssafy.nagne.web;

import static com.ssafy.nagne.security.Jwt.Claims;

import com.ssafy.nagne.entity.User;
import com.ssafy.nagne.security.Jwt;
import lombok.Data;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

@Data
public class LoginResult {

    private String token;
    private UserDto user;

    public LoginResult(Jwt jwt, Authentication authentication) {
        user = new UserDto((User) authentication.getDetails());
        token = jwt.create(
                Claims.of(
                        user.getId(),
                        user.getUsername(),
                        authentication.getAuthorities().stream()
                                .map(GrantedAuthority::getAuthority)
                                .toArray(String[]::new))
        );
    }
}
