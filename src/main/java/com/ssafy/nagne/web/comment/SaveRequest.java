package com.ssafy.nagne.web.comment;

import jakarta.validation.constraints.NotNull;

public record SaveRequest(@NotNull Long articleId, @NotNull String content) {
}
