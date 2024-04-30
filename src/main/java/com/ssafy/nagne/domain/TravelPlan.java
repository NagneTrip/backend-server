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
public class TravelPlan {

    private Long id;

    private Long userId;

    private List<Schedule> schedules;

    private LocalDateTime createdDate;

    private LocalDateTime lastModifiedDate;
}
