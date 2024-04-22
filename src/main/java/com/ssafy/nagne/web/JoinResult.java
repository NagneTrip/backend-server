package com.ssafy.nagne.web;

import com.ssafy.nagne.domain.User;
import lombok.Data;

@Data
public class JoinResult {

    private UserDto userInfo;

    public JoinResult(User user) {
        this.userInfo = new UserDto(user);
    }
}
