package com.ssafy.nagne.web.user;

import com.ssafy.nagne.domain.User;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class UserListResult {

    private int count;
    private final List<UserInfo> userInfo;

    public UserListResult(List<User> users) {
        count = users.size();
        
        userInfo = users.stream()
                .map(UserInfo::new)
                .toList();
    }

    @Data
    private static class UserInfo {

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
