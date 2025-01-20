package org.example.springbootdeveloper.article.domain.repository;

import org.example.springbootdeveloper.article.api.dto.request.SearchPostRequest;
import org.example.springbootdeveloper.article.api.dto.response.PostResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom {
    Page<PostResponse> searchPage(SearchPostRequest searchPostRequest, Pageable pageable);
}
