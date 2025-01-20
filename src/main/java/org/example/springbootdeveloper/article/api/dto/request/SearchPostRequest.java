package org.example.springbootdeveloper.article.api.dto.request;

public record SearchPostRequest(
        String title,
        String content
) {
}
