package com.ssafy.nagne.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Schedule {

    private Long travelPlanId;

    private Attraction attraction;

    private Integer attractionOrder;
}
