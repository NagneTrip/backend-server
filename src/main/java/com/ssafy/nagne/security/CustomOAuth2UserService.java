package com.ssafy.nagne.security;

import static com.ssafy.nagne.security.userinfo.OAuth2UserInfoFactory.createUserInfo;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        return process(userRequest);
    }

    private OAuth2User process(OAuth2UserRequest oAuth2UserRequest) {
        String registrationId = oAuth2UserRequest.getClientRegistration().getRegistrationId();

        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

        return new CustomOAuth2User(
                createUserInfo(registrationId, oAuth2User.getAttributes())
        );
    }
}
