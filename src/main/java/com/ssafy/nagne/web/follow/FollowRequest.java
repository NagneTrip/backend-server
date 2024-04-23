package com.ssafy.nagne.web.follow;

import jakarta.validation.constraints.NotNull;

public record FollowRequest(@NotNull Long followId) {
}
