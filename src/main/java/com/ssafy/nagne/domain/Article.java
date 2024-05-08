package com.ssafy.nagne.domain;

import static com.google.common.base.Preconditions.checkArgument;

import com.ssafy.nagne.web.article.UpdateRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Article {

    private Long id;

    private Long userId;

    private String content;

    private Integer good;

    private List<String> imageUrls;

    private LocalDateTime createdDate;

    private LocalDateTime lastModifiedDate;

    public List<String> extractHashTags() {
        List<String> tags = new ArrayList<>();

        Pattern pattern = Pattern.compile("(#[\\d|A-Z|a-z|ㄱ-ㅎ|ㅏ-ㅣ|가-힣]*)");
        Matcher matcher = pattern.matcher(this.content);

        while (matcher.find()) {
            tags.add(matcher.group());
        }

        return tags;
    }

    public boolean isMine(Long sessionId) {
        return this.userId.equals(sessionId);
    }

    public void update(UpdateRequest request) {
        this.content = request.content();
    }

    public void update(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public void like() {
        this.good++;
    }

    public void unlike() {
        this.good--;

        checkArgument(this.good >= 0, "good must be positive");
    }
}
