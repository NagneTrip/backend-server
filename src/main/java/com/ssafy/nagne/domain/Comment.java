package com.ssafy.nagne.domain;

import static com.google.common.base.Preconditions.checkArgument;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Comment {

    private Long id;

    private Long articleId;

    private Long userId;

    private String content;

    private Integer good;

    private LocalDateTime createdDate;

    private LocalDateTime lastModifiedDate;

    public boolean isMine(Long sessionId) {
        return this.userId.equals(sessionId);
    }

    public void update(String content) {
        this.content = content;
    }

    public void like() {
        this.good++;
    }

    public void unlike() {
        this.good--;

        checkArgument(this.good >= 0, "good must be positive");
    }
}
