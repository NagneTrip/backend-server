package com.ssafy.nagne.repository;

import com.ssafy.nagne.domain.TravelPlan;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TravelPlanRepository {

    Optional<TravelPlan> findById(@Param("id") Long id);
}
