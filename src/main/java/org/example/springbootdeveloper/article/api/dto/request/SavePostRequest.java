package org.example.springbootdeveloper.article.api.dto.request;

import jakarta.validation.constraints.NotNull;
import org.example.springbootdeveloper.article.domain.Post;

public record SavePostRequest(
        @NotNull
        String title,
        @NotNull
        String content
) {
    public Post toEntity() {
        return Post.builder()
                .title(title)
                .content(content)
                .build();
    }
}
