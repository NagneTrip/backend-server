package com.ssafy.nagne.web.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import org.hibernate.validator.constraints.Length;

public record JoinRequest(
        @NotNull @Email @Length(max = 50) String username,
        @NotNull @Length(max = 20) String password,
        @NotNull @Length(max = 20) String nickname,
        String phone,
        String gender,
        LocalDate birth
) {
}
