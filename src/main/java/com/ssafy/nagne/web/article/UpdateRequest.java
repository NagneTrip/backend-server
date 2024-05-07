package com.ssafy.nagne.web.article;

import jakarta.validation.constraints.NotNull;

public record UpdateRequest(@NotNull String content) {
}
