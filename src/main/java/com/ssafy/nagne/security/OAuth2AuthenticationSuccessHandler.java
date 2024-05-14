package com.ssafy.nagne.security;

import static org.springframework.security.core.authority.AuthorityUtils.createAuthorityList;

import com.ssafy.nagne.domain.User;
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
        send(authentication, response);
    }

    private void send(Authentication authentication, HttpServletResponse response)
            throws IOException {
        String username = getUsername(authentication);

        //TODO: 첫 소셜 로그인 할 때 회원 가입 할 수 있게 구현하기
        User user = userService.loginOAuth(username);

        String token = createToken(user);

        send(response, user, token);
    }

    private String getUsername(Authentication authentication) {
        DefaultOAuth2User principal = (DefaultOAuth2User) authentication.getPrincipal();

        return principal.getAttribute("email");
    }

    private String createToken(User user) {
        JwtAuthenticationToken authenticated = new JwtAuthenticationToken(
                new JwtAuthentication(user.getId(), user.getUsername()),
                null,
                createAuthorityList(Role.USER.value())
        );

        return jwt.create(
                Claims.of(
                        user.getId(),
                        user.getUsername(),
                        authenticated.getAuthorities().stream()
                                .map(GrantedAuthority::getAuthority)
                                .toArray(String[]::new)
                )
        );
    }

    private void send(HttpServletResponse response, User user, String token) throws IOException {
        String successMessage = """
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
                }""".formatted(token, user.getId(), user.getUsername(), user.getLastLoginDate());

        response.setStatus(HttpServletResponse.SC_OK);
        response.setHeader("content-type", "application/json");
        response.getWriter().write(successMessage);
        response.getWriter().flush();
        response.getWriter().close();
    }
}
