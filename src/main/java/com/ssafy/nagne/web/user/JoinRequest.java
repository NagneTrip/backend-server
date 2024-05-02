package com.ssafy.nagne.web.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record JoinRequest(
        @NotNull @Email String username,
        @NotNull String password,
        @NotNull String nickname,
        String phone,
        String gender,
        LocalDate birth
) {
}
