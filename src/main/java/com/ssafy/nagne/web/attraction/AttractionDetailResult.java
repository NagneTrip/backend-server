package com.ssafy.nagne.web.attraction;

import static org.springframework.beans.BeanUtils.copyProperties;

import com.ssafy.nagne.domain.Attraction;
import lombok.Data;

@Data
public class AttractionDetailResult {

    private final AttractionInfo attractionInfo;

    public AttractionDetailResult(Attraction attraction) {
        this.attractionInfo = new AttractionInfo(attraction);
    }

    @Data
    private static class AttractionInfo {

        private Long id;
        private Long attractionTypeId;
        private Long sidoCode;
        private Long gugunCode;
        private String title;
        private String addr;
        private String zipCode;
        private String tel;
        private String imageUrl;
        private Double latitude;
        private Double longitude;

        public AttractionInfo(Attraction attraction) {
            copyProperties(attraction, this);
        }
    }
}
