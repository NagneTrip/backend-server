package com.ssafy.nagne.web.travelplan;

import com.ssafy.nagne.domain.TravelPlan;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class SaveResult {

    private final TravelPlanInfo travelPlanInfo;

    public SaveResult(TravelPlan travelPlan) {
        this.travelPlanInfo = new TravelPlanInfo(travelPlan);
    }

    @Data
    private static class TravelPlanInfo {

        private final Long id;
        private final Long userId;
        private final LocalDateTime createdDate;

        public TravelPlanInfo(TravelPlan travelPlan) {
            this.id = travelPlan.getId();
            this.userId = travelPlan.getUserId();
            this.createdDate = travelPlan.getCreatedDate();
        }
    }
}
