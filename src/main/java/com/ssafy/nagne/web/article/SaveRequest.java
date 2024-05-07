package com.ssafy.nagne.web.article;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record SaveRequest(@NotNull @Length(max = 1000) String content) {
}
