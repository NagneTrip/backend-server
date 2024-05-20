package com.ssafy.nagne.repository;

import com.ssafy.nagne.domain.Notification;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query(value = "SELECT notification.*, "
            + "users.nickname AS from_user_nickname, "
            + "users.profile_image AS from_user_profile_image, "
            + "users.tier AS from_user_tier "
            + "FROM notification LEFT JOIN users ON notification.from_user_id = users.id "
            + "WHERE to_user_id = :toUserId", nativeQuery = true)
    List<Notification> findNotificationsByToUserId(@Param("toUserId") Long toUserId);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE notification SET is_new = false WHERE id = :id", nativeQuery = true)
    int read(@Param("id") Long id);
}
