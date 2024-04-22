package com.ssafy.nagne.web.user;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record JoinRequest(
        @NotNull String username,
        @NotNull String password,
        @NotNull String nickname,
        String email,
        String phone,
        String gender,
        @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul") LocalDate birth
) {
}
