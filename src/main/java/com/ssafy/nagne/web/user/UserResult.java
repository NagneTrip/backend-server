package com.ssafy.nagne.web.user;

import static org.springframework.beans.BeanUtils.copyProperties;

import com.ssafy.nagne.domain.Tier;
import com.ssafy.nagne.domain.User;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class UserResult {

    private final UserInfo userInfo;

    public UserResult(User user) {
        this.userInfo = new UserInfo(user);
    }

    @Data
    private static class UserInfo {

        private Long id;
        private String username;
        private String nickname;
        private Integer followers;
        private Integer followings;
        private String profileImage;
        private Tier tier;
        private LocalDateTime lastLoginDate;

        public UserInfo(User user) {
            copyProperties(user, this);
        }
    }
}
