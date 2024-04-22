package com.ssafy.nagne.web.user;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;

public record UpdateRequest(
        String nickname,
        String email,
        String phone,
        @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul") LocalDate birth,
        String gender
) {
}
