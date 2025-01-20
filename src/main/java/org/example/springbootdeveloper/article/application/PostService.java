package org.example.springbootdeveloper.article.application;

import lombok.RequiredArgsConstructor;
import org.example.springbootdeveloper.article.api.dto.request.SavePostRequest;
import org.example.springbootdeveloper.article.api.dto.request.SearchPostRequest;
import org.example.springbootdeveloper.article.api.dto.response.PostResponse;
import org.example.springbootdeveloper.article.domain.Post;
import org.example.springbootdeveloper.article.domain.repository.PostRepository;
import org.example.springbootdeveloper.article.domain.repository.PostRepositoryCustom;
import org.example.springbootdeveloper.article.exception.NotFoundPostException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final PostRepositoryCustom postRepositoryCustom;

    @Transactional
    public PostResponse save(SavePostRequest saveArticleRequest) {
        Post post = postRepository.save(saveArticleRequest.toEntity());
        return PostResponse.toDto(post);
    }

    @Transactional
    public void deleteAll() {
        postRepository.deleteAll();
    }

    public List<PostResponse> findAll() {
        List<Post> posts = postRepository.findAll();
        return posts.stream()
                .map(PostResponse::toDto)
                .collect(Collectors.toList());
    }

    public Page<PostResponse> searchAndPagePost(SearchPostRequest searchPostRequest, Pageable pageable) {
        return postRepositoryCustom.searchPage(searchPostRequest, pageable);
    }

    public PostResponse findById(Long id) {
        Post post = getPost(id);
        return PostResponse.toDto(post);
    }

    @Transactional
    public void deleteById(Long id) {
        if (getPost(id) != null) {
            postRepository.deleteById(id);
        }
    }

    @Transactional
    public PostResponse updatePost(Long id, SavePostRequest savePostRequest) {
        Post post = getPost(id);
        post.update(savePostRequest);
        return PostResponse.toDto(post);
    }

    private Post getPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new NotFoundPostException("not foubd: " + id));
        return post;
    }
}
