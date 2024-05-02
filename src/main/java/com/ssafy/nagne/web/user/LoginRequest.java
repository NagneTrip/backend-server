package com.ssafy.nagne.web.user;

import jakarta.validation.constraints.NotNull;

public record LoginRequest(@NotNull(message = "username must be provided") String username,
                           @NotNull(message = "password must be provided") String password) {
}
