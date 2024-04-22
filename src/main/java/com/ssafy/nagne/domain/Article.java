package com.ssafy.nagne.domain;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Article {

    private Long id;

    private Long user_id;

    private String title;

    private String content;

    private int hit;

    private int good;

    private LocalDateTime createdDate;

    private LocalDateTime lastModifiedDate;
}
