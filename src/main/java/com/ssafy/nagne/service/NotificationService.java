package com.ssafy.nagne.service;

import com.ssafy.nagne.domain.Notification;
import com.ssafy.nagne.repository.NotificationRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public void save(Notification notification) {
        notificationRepository.save(notification);
    }

    public List<Notification> findNotificationsByUserId(Long userId) {
        return notificationRepository.findNotificationsByToUserId(userId);
    }
}
