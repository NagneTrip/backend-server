package com.ssafy.nagne.repository;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ScheduleRepository {

    void save(@Param("travelPlanId") Long travelPlanId, @Param("attractions") List<Long> attractions);

    void delete(@Param("travelPlanId") Long travelPlanId);
}
