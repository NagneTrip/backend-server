package com.ssafy.nagne.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@Entity
@DiscriminatorValue("F")
@SuperBuilder
@NoArgsConstructor
public class FollowNotification extends Notification {
}
