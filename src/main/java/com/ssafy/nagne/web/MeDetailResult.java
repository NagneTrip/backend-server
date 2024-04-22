package com.ssafy.nagne.web;

import static org.springframework.beans.BeanUtils.copyProperties;

import com.ssafy.nagne.domain.Gender;
import com.ssafy.nagne.domain.Tier;
import com.ssafy.nagne.domain.User;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class MeDetailResult {

    private final UserInfo userInfo;

    public MeDetailResult(User user) {
        this.userInfo = new UserInfo(user);
    }

    @Data
    public static class UserInfo {

        private Long id;
        private String username;
        private String nickname;
        private String email;
        private String phone;
        private LocalDate birth;
        private Gender gender;
        private String profileImage;
        private Tier tier;
        private LocalDateTime createdDate;
        private LocalDateTime lastLoginDate;

        public UserInfo(User user) {
            copyProperties(user, this);
        }
    }
}
