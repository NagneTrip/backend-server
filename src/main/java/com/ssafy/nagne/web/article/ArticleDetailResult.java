package com.ssafy.nagne.web.article;

import static org.springframework.beans.BeanUtils.copyProperties;

import com.ssafy.nagne.domain.Article;
import com.ssafy.nagne.domain.Image;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class ArticleDetailResult {

    private final ArticleInfo articleInfo;

    public ArticleDetailResult(Article article) {
        this.articleInfo = new ArticleInfo(article);
    }

    @Data
    private static class ArticleInfo {

        private Long id;
        private Long userId;
        private String content;
        private Integer good;
        private List<String> imageUrls;
        private LocalDateTime createdDate;

        public ArticleInfo(Article article) {
            copyProperties(article, this);

            imageUrls = article.getImageUrls().stream()
                    .map(Image::getUrl)
                    .toList();
        }
    }
}
