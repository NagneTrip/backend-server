package com.ssafy.nagne.web.travelplan;

import jakarta.validation.constraints.Size;
import java.util.List;

public record SaveUpdateRequest(@Size(min = 1, max = 20) List<Long> attractions) {
}
