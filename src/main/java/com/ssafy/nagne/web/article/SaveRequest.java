package com.ssafy.nagne.web.article;

import jakarta.validation.constraints.NotNull;

public record SaveRequest(@NotNull String title, @NotNull String content) {
}
