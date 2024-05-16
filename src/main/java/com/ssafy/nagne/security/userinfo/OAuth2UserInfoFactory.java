package com.ssafy.nagne.security.userinfo;

import java.util.Map;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo createUserInfo(String registrationId, Map<String, Object> attributes) {
        try {
            Class<?> clazz = Class.forName("com.ssafy.nagne.security.userinfo." +
                    capitalizeFirstLetter(registrationId) +
                    "OAuth2UserInfo");

            return (OAuth2UserInfo) clazz.getDeclaredConstructor(Map.class).newInstance(attributes);
        } catch (Exception e) {
            throw new OAuth2AuthenticationException("reflection error");
        }
    }

    private static String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }

        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
