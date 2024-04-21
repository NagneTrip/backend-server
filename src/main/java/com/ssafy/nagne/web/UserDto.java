package com.ssafy.nagne.web;

import com.ssafy.nagne.entity.User;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class UserDto {

    private final Long id;
    private final String username;
    private final LocalDateTime lastLoginDate;

    public UserDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.lastLoginDate = user.getLastLoginDate();
    }
}