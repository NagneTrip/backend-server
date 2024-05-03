package com.ssafy.nagne.web.user;

import java.time.LocalDate;

public record UpdateRequest(
        String nickname,
        String phone,
        LocalDate birth,
        String gender
) {
}
