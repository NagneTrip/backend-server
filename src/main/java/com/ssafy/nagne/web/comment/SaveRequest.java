package com.ssafy.nagne.web.comment;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record SaveRequest(@NotNull Long articleId,
                          @NotNull @Length(max = 200) String content) {
}
