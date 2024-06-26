package com.ssafy.nagne.domain;

import static java.time.LocalDateTime.now;

import com.ssafy.nagne.web.user.UpdateRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private Long id;

    private String username;

    private String password;

    private String nickname;

    private Boolean isFollowed;

    private Integer followers;

    private Integer followings;

    private String phone;

    private LocalDate birth;

    private Gender gender;

    private String profileImage;

    private Tier tier;

    private LocalDateTime createdDate;

    private LocalDateTime lastModifiedDate;

    private LocalDateTime lastLoginDate;

    public void checkPassword(PasswordEncoder passwordEncoder, String password) {
        if (!passwordEncoder.matches(password, this.password)) {
            throw new IllegalArgumentException();
        }
    }

    public void afterLogin() {
        this.lastLoginDate = now();
    }

    public boolean isMe(Long sessionId) {
        return this.id.equals(sessionId);
    }

    public void updateInfo(UpdateRequest request, String profileImage) {
        this.nickname = request.nickname();
        this.phone = request.phone();
        this.profileImage = profileImage;
    }
}
