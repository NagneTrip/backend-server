package com.ssafy.nagne.security.userinfo;

import java.util.Map;

public class KakaoOAuth2UserInfo implements OAuth2UserInfo {

    private final Map<String, Object> attributes;
    private final String registrationId;
    private final String username;

    public KakaoOAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
        this.registrationId = "kakao";

        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        this.username = (String) kakaoAccount.get("email");
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getRegistrationId() {
        return registrationId;
    }

    @Override
    public String getUsername() {
        return username;
    }
}
