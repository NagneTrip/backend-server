package com.ssafy.nagne.web;

import jakarta.validation.constraints.NotNull;

public record LoginRequest(@NotNull String principal, @NotNull String credentials) {
}
