package com.ssafy.nagne.service;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.time.LocalDateTime.now;

import com.ssafy.nagne.domain.TravelPlan;
import com.ssafy.nagne.error.AccessDeniedException;
import com.ssafy.nagne.error.NotFoundException;
import com.ssafy.nagne.repository.ScheduleRepository;
import com.ssafy.nagne.repository.TravelPlanRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class TravelPlanService {

    private final TravelPlanRepository travelPlanRepository;
    private final ScheduleRepository scheduleRepository;

    public TravelPlan save(List<Long> attractions, Long userId) {
        checkArgument(attractions.size() <= 20, "must be 20 attractions or less");

        TravelPlan newTravelPlan = createNewTravelPlan(userId);

        return save(newTravelPlan, attractions);
    }

    @Transactional(readOnly = true)
    public TravelPlan findById(Long id) {
        checkNotNull(id, "id must be provided");

        return travelPlanRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Could not found article for " + id));
    }

    public boolean update(Long id, Long sessionId, List<Long> attractions) {
        checkNotNull(id, "id must be provided");

        findTravelPlanAndCheckMine(id, sessionId);

        return update(id, attractions);
    }

    public boolean delete(Long id, Long sessionId) {
        checkNotNull(id, "id must be provided");

        findTravelPlanAndCheckMine(id, sessionId);

        return delete(id);
    }

    private TravelPlan createNewTravelPlan(Long userId) {
        return TravelPlan.builder()
                .userId(userId)
                .createdDate(now())
                .build();
    }

    private TravelPlan save(TravelPlan newTravelPlan, List<Long> attractions) {
        travelPlanRepository.save(newTravelPlan);

        scheduleRepository.save(newTravelPlan.getId(), attractions);

        return newTravelPlan;
    }

    private boolean update(Long id, List<Long> attractions) {
        scheduleRepository.delete(id);
        scheduleRepository.save(id, attractions);

        return travelPlanRepository.update(id) == 1;
    }

    private boolean delete(Long id) {
        scheduleRepository.delete(id);

        return travelPlanRepository.delete(id) == 1;
    }

    private TravelPlan findTravelPlanAndCheckMine(Long id, Long sessionId) {
        TravelPlan travelPlan = findById(id);

        if (!travelPlan.isMine(sessionId)) {
            throw new AccessDeniedException();
        }

        return travelPlan;
    }
}
