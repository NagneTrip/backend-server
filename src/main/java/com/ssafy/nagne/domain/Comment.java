package com.ssafy.nagne.domain;

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
}
