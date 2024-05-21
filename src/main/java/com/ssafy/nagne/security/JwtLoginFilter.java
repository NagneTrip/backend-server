package com.ssafy.nagne.security;

import static com.ssafy.nagne.api.ApiUtils.error;
import static com.ssafy.nagne.api.ApiUtils.success;
import static com.ssafy.nagne.security.Jwt.Claims.of;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.nagne.domain.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtLoginFilter extends AbstractAuthenticationProcessingFilter {

    private static final AntPathRequestMatcher ANT_PATH_REQUEST_MATCHER = new AntPathRequestMatcher("/api/users/login",
            "POST");

    private final ObjectMapper objectMapper;
    private final Jwt jwt;

    public JwtLoginFilter(AuthenticationManager authenticationManager, ObjectMapper objectMapper, Jwt jwt) {
        super(ANT_PATH_REQUEST_MATCHER, authenticationManager);

        this.objectMapper = objectMapper;
        this.jwt = jwt;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException {
        LoginRequest loginRequest = objectMapper.readValue(request.getInputStream(), LoginRequest.class);

        JwtAuthenticationToken jwtAuthenticationToken = new JwtAuthenticationToken(loginRequest.username(),
                loginRequest.password());

        return getAuthenticationManager().authenticate(jwtAuthenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException {
        LoginResult loginResult = new LoginResult(createJWT(authResult), (User) authResult.getDetails());

        response.setStatus(HttpServletResponse.SC_OK);
        response.setHeader("content-type", "application/json");
        objectMapper.writeValue(response.getOutputStream(), success(loginResult));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setHeader("content-type", "application/json");
        objectMapper.writeValue(response.getOutputStream(), error("Bad Credential", HttpStatus.UNAUTHORIZED));
    }

    private String createJWT(Authentication authResult) {
        User user = (User) authResult.getDetails();

        return jwt.create(of(
                user.getId(),
                user.getUsername(),
                authResult.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .toArray(String[]::new))
        );
    }

    private record LoginRequest(String username, String password) {
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
}
