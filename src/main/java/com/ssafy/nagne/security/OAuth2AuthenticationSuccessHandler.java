package com.ssafy.nagne.security;

import static org.springframework.security.core.authority.AuthorityUtils.createAuthorityList;

import com.ssafy.nagne.domain.User;
import com.ssafy.nagne.error.NotFoundException;
import com.ssafy.nagne.security.Jwt.Claims;
import com.ssafy.nagne.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final Jwt jwt;
    private final UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        String username = getUsername(authentication);

        try {
            User user = login(username);
            send(response, createSuccessMessage(user));
        } catch (NotFoundException e) {
            send(response, createNeedToJoinMessage(username));
        }
    }

    private User login(String username) {
        return userService.loginOAuth(username);
    }

    private String getUsername(Authentication authentication) {
        DefaultOAuth2User principal = (DefaultOAuth2User) authentication.getPrincipal();
        return principal.getAttribute("email");
    }

    private String createSuccessMessage(User user) {
        JwtAuthenticationToken authenticated = createAuthenticationToken(user);

        String jwt = createJWT(authenticated);

        return """
                {
                    "success": true,
                    "response": {
                        "token": "%s",
                        "userInfo": {
                            "id": %d,
                            "username": "%s",
                            "lastLoginDate": "%s"
                        }
                    },
                    "error": null
                }""".formatted(jwt, user.getId(), user.getUsername(), user.getLastLoginDate());
    }

    private JwtAuthenticationToken createAuthenticationToken(User user) {
        return new JwtAuthenticationToken(
                new JwtAuthentication(user.getId(), user.getUsername()),
                null,
                createAuthorityList(Role.USER.value())
        );
    }

    private String createJWT(JwtAuthenticationToken authenticated) {
        JwtAuthentication principal = (JwtAuthentication) authenticated.getPrincipal();

        return jwt.create(
                Claims.of(
                        principal.id(),
                        principal.username(),
                        authenticated.getAuthorities().stream()
                                .map(GrantedAuthority::getAuthority)
                                .toArray(String[]::new)
                )
        );
    }

    private String createNeedToJoinMessage(String username) {
        return """
                {
                    "success": true,
                    "response": {
                        "needToJoin": true,
                        "username": "%s"
                    },
                    "error": null
                }""".formatted(username);
    }

    private void send(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setHeader("content-type", "application/json");
        response.getWriter().write(message);
        response.getWriter().flush();
        response.getWriter().close();
    }
}
