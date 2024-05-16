package com.ssafy.nagne.security.userinfo;

import java.util.Map;

public interface OAuth2UserInfo {

    Map<String, Object> getAttributes();

    String getRegistrationId();

    String getUsername();
}
