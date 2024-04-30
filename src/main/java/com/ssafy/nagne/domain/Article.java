package com.ssafy.nagne.domain;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Article {

    private Long id;

    private Long userId;

    private String content;

    private Integer good;

    private List<Image> imageUrls;

    private LocalDateTime createdDate;

    private LocalDateTime lastModifiedDate;
}
