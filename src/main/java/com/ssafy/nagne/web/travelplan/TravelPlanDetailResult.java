package com.ssafy.nagne.web.travelplan;

import static org.springframework.beans.BeanUtils.copyProperties;

import com.ssafy.nagne.domain.Attraction;
import com.ssafy.nagne.domain.Schedule;
import com.ssafy.nagne.domain.TravelPlan;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class TravelPlanDetailResult {

    private TravelPlanInfo travelPlanInfo;

    public TravelPlanDetailResult(TravelPlan travelPlan) {
        travelPlanInfo = new TravelPlanInfo(travelPlan);
    }

    @Data
    private static class TravelPlanInfo {

        private Long id;
        private Long userId;
        private String title;
        private List<AttractionInfo> attractions;
        private LocalDateTime createdDate;

        public TravelPlanInfo(TravelPlan travelPlan) {
            BeanUtils.copyProperties(travelPlan, this);

            attractions = travelPlan.getSchedules().stream()
                    .sorted(Comparator.comparingInt(Schedule::getAttractionOrder))
                    .map(Schedule::getAttraction)
                    .map(AttractionInfo::new)
                    .toList();
        }
    }

    @Data
    private static class AttractionInfo {

        private Long id;
        private String title;
        private String addr;
        private Double latitude;
        private Double longitude;

        public AttractionInfo(Attraction attraction) {
            copyProperties(attraction, this);
        }
    }
}
