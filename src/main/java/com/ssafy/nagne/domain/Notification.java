package com.ssafy.nagne.domain;

import java.time.LocalDateTime;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
public abstract class Notification {

    private Long id;

    private NotificationType type;

    private Long fromUserId;

    private Long toUserId;

    private Boolean isNew;

    private LocalDateTime createdDate;
}
