package com.ssafy.nagne.repository;

import com.ssafy.nagne.domain.Notification;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query("SELECT n FROM Notification n WHERE n.toUserId = :toUserId")
    List<Notification> findNotificationsByToUserId(@Param("toUserId") Long toUserId);
}
