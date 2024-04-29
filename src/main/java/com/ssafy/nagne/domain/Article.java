package com.ssafy.nagne.domain;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Article {

    private Long id;

    private Long userId;

    private String content;

    private Integer good;

    private LocalDateTime createdDate;

    private LocalDateTime lastModifiedDate;
}
