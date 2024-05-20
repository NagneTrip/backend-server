package com.ssafy.nagne.repository;

import com.ssafy.nagne.domain.Notification;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findNotificationsByToUserId(@Param("toUserId") Long toUserId);

    @Query("SELECT COUNT(n) FROM Notification n WHERE n.toUserId = :toUserId AND n.isNew")
    int countNewNotifications(@Param("toUserId") Long toUserId);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE notification SET is_new = false WHERE id = :id", nativeQuery = true)
    int read(@Param("id") Long id);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE notification SET is_new = false WHERE to_user_id = :userId", nativeQuery = true)
    int readAll(@Param("userId") Long userId);
}
