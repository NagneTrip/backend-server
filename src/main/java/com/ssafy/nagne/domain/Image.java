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
public class Image {

    private Long articleId;

    private String url;

    private LocalDateTime createdDate;
}
