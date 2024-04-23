package com.ssafy.nagne.web.bookmark;

import jakarta.validation.constraints.NotNull;

public record BookmarkRequest(@NotNull Long articleId) {
}
