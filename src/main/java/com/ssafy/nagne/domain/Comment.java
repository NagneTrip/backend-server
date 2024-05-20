package com.ssafy.nagne.domain;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    private Long id;

    private Long articleId;

    private Long userId;

    private String userNickname;

    private String userProfileImage;

    private Tier userTier;

    private String content;

    private Integer likeCount;

    private Boolean isLiked;

    private LocalDateTime createdDate;

    private LocalDateTime lastModifiedDate;

    public boolean isMine(Long sessionId) {
        return this.userId.equals(sessionId);
    }

    public void update(String content) {
        this.content = content;
    }
}
