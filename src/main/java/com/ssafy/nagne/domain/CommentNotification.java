package com.ssafy.nagne.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@Entity
@DiscriminatorValue("C")
@SuperBuilder
@NoArgsConstructor
public class CommentNotification extends Notification {

    private Long articleId;

    private Long commentId;
}
