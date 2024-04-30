package com.ssafy.nagne.service;

import static lombok.Lombok.checkNotNull;

import com.ssafy.nagne.domain.TravelPlan;
import com.ssafy.nagne.error.NotFoundException;
import com.ssafy.nagne.repository.ScheduleRepository;
import com.ssafy.nagne.repository.TravelPlanRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TravelPlanService {

    private final TravelPlanRepository travelPlanRepository;
    private final ScheduleRepository scheduleRepository;

    @Transactional
    public TravelPlan save(TravelPlan travelPlan, List<Long> attractions) {
        travelPlanRepository.save(travelPlan);

        scheduleRepository.save(travelPlan.getId(), attractions);
        
        return travelPlan;
    }

    public TravelPlan findById(Long id) {
        checkNotNull(id, "id must be provided");

        return travelPlanRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Could not found article for " + id));
    }
}
