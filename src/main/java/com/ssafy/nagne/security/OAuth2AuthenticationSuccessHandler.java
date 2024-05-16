package com.ssafy.nagne.security;

import static com.ssafy.nagne.security.Jwt.Claims.of;
import static com.ssafy.nagne.utils.ApiUtils.success;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.nagne.domain.User;
import com.ssafy.nagne.error.NotFoundException;
import com.ssafy.nagne.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final ObjectMapper objectMapper;
    private final Jwt jwt;

    private final UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        String username = getUsername(authentication);

        try {
            User user = userService.loginOAuth(username);
            String jwt = createJWT(user);

            send(response, new LoginResult(jwt, user));
        } catch (NotFoundException e) {
            send(response, new NeedToJoinResult(username));
        }
    }

    private String getUsername(Authentication authentication) {
        CustomOAuth2User principal = (CustomOAuth2User) authentication.getPrincipal();
        return principal.getName();
    }

    private String createJWT(User user) {
        return jwt.create(of(
                user.getId(),
                user.getUsername(),
                new String[]{Role.USER.value()})
        );
    }

    private <T> void send(HttpServletResponse response, T data) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setHeader("content-type", "application/json");
        objectMapper.writeValue(response.getOutputStream(), success(data));
    }

    private record LoginResult(String token, UserInfo userInfo) {

        public LoginResult(String token, User user) {
            this(token, new UserInfo(user));
        }

        private record UserInfo(Long id, String username, LocalDateTime lastLoginDate) {
            private UserInfo(User user) {
                this(user.getId(), user.getUsername(), user.getLastLoginDate());
            }
        }
    }

    private record NeedToJoinResult(boolean needToJoin, String username) {

        public NeedToJoinResult(String username) {
            this(true, username);
        }
    }
}
