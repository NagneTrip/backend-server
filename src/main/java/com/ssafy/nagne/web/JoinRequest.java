package com.ssafy.nagne.web;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

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
