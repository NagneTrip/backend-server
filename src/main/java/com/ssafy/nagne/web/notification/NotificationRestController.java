package com.ssafy.nagne.web.notification;

import com.ssafy.nagne.domain.Notification;
import com.ssafy.nagne.security.JwtAuthentication;
import com.ssafy.nagne.service.NotificationService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
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
}
