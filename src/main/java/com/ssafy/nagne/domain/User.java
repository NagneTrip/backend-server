package com.ssafy.nagne.domain;

import static java.time.LocalDateTime.now;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class User {

    private Long id;

    private String username;

    private String password;

    private String nickname;

    private String email;

    private String phone;

    private LocalDate birth;

    private Gender gender;

    private String profileImage;

    private Tier tier;

    private LocalDateTime createdDate;

    private LocalDateTime lastModifiedDate;

    private LocalDateTime lastLoginDate;

    public void afterLogin() {
        lastLoginDate = now();
    }

    public void encodePassword(String encodedPassword) {
        this.password = encodedPassword;
    }
}
