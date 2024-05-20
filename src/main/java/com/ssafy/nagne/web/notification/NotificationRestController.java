package com.ssafy.nagne.web.notification;

import com.ssafy.nagne.domain.Notification;
import com.ssafy.nagne.security.JwtAuthentication;
import com.ssafy.nagne.service.NotificationService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("api/notifications")
@RequiredArgsConstructor
public class NotificationRestController {

    private final NotificationService notificationService;

    @GetMapping
    public List<Notification> findNotificationsByUserId(@AuthenticationPrincipal JwtAuthentication authentication) {
        return notificationService.findNotificationsByUserId(authentication.id());
    }

    @GetMapping("/has-new")
    public Boolean hasNewNotifications(@AuthenticationPrincipal JwtAuthentication authentication) {
        return notificationService.hasNewNotifications(authentication.id());
    }

    //TODO: 자신의 알림만 읽을 수 있도록 하기
    @PatchMapping("/{id}")
    public Boolean read(@PathVariable Long id) {
        return notificationService.read(id);
    }

    @PatchMapping
    public Boolean readAll(@AuthenticationPrincipal JwtAuthentication authentication) {
        return notificationService.readAll(authentication.id());
    }
}
