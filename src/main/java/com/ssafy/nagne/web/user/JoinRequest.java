package com.ssafy.nagne.web.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record JoinRequest(
        @NotNull(message = "username must be provided") @Email(message = "username must be a well-formed email address") String username,
        @NotNull(message = "password must be provided") String password,
        @NotNull(message = "nickname must be provided") String nickname,
        String phone,
        String gender,
        LocalDate birth
) {
}
