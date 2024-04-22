package com.ssafy.nagne.web;

import static com.ssafy.nagne.security.Jwt.Claims;

import com.ssafy.nagne.domain.User;
import com.ssafy.nagne.security.Jwt;
import java.time.LocalDateTime;
import lombok.Data;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

@Data
public class LoginResult {

    private final String token;
    private final UserInfo userInfo;

    public LoginResult(Jwt jwt, Authentication authentication) {
        this.userInfo = new UserInfo((User) authentication.getDetails());
        this.token = jwt.create(
                Claims.of(
                        userInfo.getId(),
                        userInfo.getUsername(),
                        authentication.getAuthorities().stream()
                                .map(GrantedAuthority::getAuthority)
                                .toArray(String[]::new))
        );
    }

    @Data
    public static class UserInfo {

        private final Long id;
        private final String username;
        private final LocalDateTime lastLoginDate;

        public UserInfo(User user) {
            this.id = user.getId();
            this.username = user.getUsername();
            this.lastLoginDate = user.getLastLoginDate();
        }
    }
}
