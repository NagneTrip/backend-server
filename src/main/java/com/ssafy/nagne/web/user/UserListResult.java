package com.ssafy.nagne.web.user;

import static org.springframework.beans.BeanUtils.copyProperties;

import com.ssafy.nagne.domain.User;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class UserListResult {

    private final List<UserInfo> userInfo;

    public UserListResult(List<User> users) {
        userInfo = users.stream()
                .map(UserInfo::new)
                .toList();
    }

    @Data
    private static class UserInfo {

        private Long id;
        private String username;
        private String nickname;
        private Integer followers;
        private Integer followings;
        private String profileImage;
        private LocalDateTime lastLoginDate;

        public UserInfo(User user) {
            copyProperties(user, this);
        }
    }
}
