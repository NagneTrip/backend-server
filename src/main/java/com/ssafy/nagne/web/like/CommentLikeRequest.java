package com.ssafy.nagne.web.like;

import jakarta.validation.constraints.NotNull;

public record CommentLikeRequest(@NotNull Long commentId) {
}
