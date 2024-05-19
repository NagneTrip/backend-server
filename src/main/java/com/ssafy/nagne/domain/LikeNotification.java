package com.ssafy.nagne.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@Entity
@DiscriminatorValue("L")
@SuperBuilder
@NoArgsConstructor
public class LikeNotification extends Notification {

    private Long articleId;
}
