package com.ssafy.nagne.repository;

import com.ssafy.nagne.domain.Notification;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface NotificationRepository {

    void save(@Param("notification") Notification notification);
}
