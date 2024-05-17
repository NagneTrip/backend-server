package com.ssafy.nagne.domain;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public abstract class Notification {

    private Long id;

    private NotificationType type;

    private Long fromUserId;

    private Long toUserId;

    private Boolean isNew;

    private LocalDateTime createdDate;
}
