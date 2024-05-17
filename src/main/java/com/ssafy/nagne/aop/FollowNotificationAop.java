package com.ssafy.nagne.aop;

import static com.ssafy.nagne.domain.NotificationType.FOLLOW;
import static java.time.LocalDateTime.now;

import com.ssafy.nagne.domain.FollowNotification;
import com.ssafy.nagne.domain.Notification;
import com.ssafy.nagne.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class FollowNotificationAop {

    private final NotificationService notificationService;

    @AfterReturning(value = "execution(* com.ssafy.nagne.service.FollowService.follow(..)) && args(id, followId)")
    public void sendFollowNotification(Long id, Long followId) {
        notificationService.save(createNotification(id, followId));
    }

    private Notification createNotification(Long id, Long followId) {
        return FollowNotification.builder()
                .type(FOLLOW)
                .fromUserId(id)
                .toUserId(followId)
                .isNew(true)
                .createdDate(now())
                .build();
    }
}
