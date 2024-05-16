package com.ssafy.nagne.security.userinfo;

import java.util.Map;

public class GoogleOAuth2UserInfo implements OAuth2UserInfo {

    private final Map<String, Object> attributes;
    private final String registrationId;
    private final String username;

    public GoogleOAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
        this.registrationId = "google";
        this.username = (String) attributes.get("email");
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
