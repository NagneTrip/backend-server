package com.ssafy.nagne.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Attraction {

    private Long id;

    private Long sidoCode;

    private Long gugunCode;

    private String title;

    private String addr;

    private String zipCode;

    private String tel;

    private String imageUrl;

    private Double latitude;

    private Double longitude;
}
