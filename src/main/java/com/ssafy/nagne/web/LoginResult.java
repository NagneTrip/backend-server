package com.ssafy.nagne.web;

import static com.ssafy.nagne.security.Jwt.Claims;

import com.ssafy.nagne.domain.User;
import com.ssafy.nagne.security.Jwt;
import lombok.Data;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

@Data
public class LoginResult {

    private String token;
    private UserDto userInfo;

    public LoginResult(Jwt jwt, Authentication authentication) {
        this.userInfo = new UserDto((User) authentication.getDetails());
        this.token = jwt.create(
                Claims.of(
                        userInfo.getId(),
                        userInfo.getUsername(),
                        authentication.getAuthorities().stream()
                                .map(GrantedAuthority::getAuthority)
                                .toArray(String[]::new))
        );
    }
}
