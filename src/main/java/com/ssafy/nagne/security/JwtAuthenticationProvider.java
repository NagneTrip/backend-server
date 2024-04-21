package com.ssafy.nagne.security;

import static org.springframework.security.core.authority.AuthorityUtils.createAuthorityList;
import static org.springframework.util.TypeUtils.isAssignable;

import com.ssafy.nagne.entity.User;
import com.ssafy.nagne.error.NotFoundException;
import com.ssafy.nagne.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final UserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) authentication;

        return processUserAuthentication(
                String.valueOf(authenticationToken.getPrincipal()),
                authenticationToken.getCredentials()
        );
    }

    private Authentication processUserAuthentication(String username, String password) {
        try {
            User user = userService.login(username, password);

            JwtAuthenticationToken authenticated = new JwtAuthenticationToken(
                    new JwtAuthentication(user.getId(), user.getUsername()),
                    null,
                    createAuthorityList(Role.USER.value())
            );

            authenticated.setDetails(user);

            return authenticated;
        } catch (NotFoundException e) {
            throw new UsernameNotFoundException(e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new BadCredentialsException(e.getMessage());
        } catch (DataAccessException e) {
            throw new AuthenticationServiceException(e.getMessage(), e);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return isAssignable(JwtAuthenticationToken.class, authentication);
    }

}