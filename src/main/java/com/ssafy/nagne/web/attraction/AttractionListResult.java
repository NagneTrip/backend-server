package com.ssafy.nagne.web.attraction;

import static org.springframework.beans.BeanUtils.copyProperties;

import com.ssafy.nagne.domain.Attraction;
import java.util.List;
import lombok.Data;

@Data
public class AttractionListResult {

    private List<AttractionInfo> attractions;

    public AttractionListResult(List<Attraction> attractions) {
        this.attractions = attractions.stream()
                .map(AttractionInfo::new)
                .toList();
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
