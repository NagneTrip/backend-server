package com.ssafy.nagne.web.user;

import com.ssafy.nagne.domain.User;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class MeResult {

    private final UserInfo userInfo;

    public MeResult(User user) {
        this.userInfo = new UserInfo(user);
    }

    @Data
    public static class UserInfo {

        private final Long id;
        private final String username;
        private final String nickname;
        private final String profileImage;
        private final LocalDateTime lastLoginDate;

        public UserInfo(User user) {
            this.id = user.getId();
            this.username = user.getUsername();
            this.nickname = user.getNickname();
            this.profileImage = user.getProfileImage();
            this.lastLoginDate = user.getLastLoginDate();
        }
    }
}
