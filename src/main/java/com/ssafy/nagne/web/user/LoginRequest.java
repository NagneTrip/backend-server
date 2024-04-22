package com.ssafy.nagne.web.user;

import jakarta.validation.constraints.NotNull;

public record LoginRequest(@NotNull String principal, @NotNull String credentials) {
}
