package org.example.springbootdeveloper.article.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.springbootdeveloper.article.api.dto.request.SavePostRequest;
import org.example.springbootdeveloper.article.api.dto.request.SearchPostRequest;
import org.example.springbootdeveloper.article.api.dto.response.PostResponse;
import org.example.springbootdeveloper.article.application.PostService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostController {

    private final PostService postService;

    @PostMapping("/post")
    public ResponseEntity<PostResponse> savePost(@RequestBody @Valid SavePostRequest savePostRequest) {
        PostResponse postResponse = postService.save(savePostRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(postResponse);
    }

    @GetMapping("/posts")
    public ResponseEntity<List<PostResponse>> findAllPosts() {
        List<PostResponse> posts = postService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(posts);
    }

    @GetMapping("/post/{id}")
    public ResponseEntity<PostResponse> findPost(@PathVariable("id") Long id) {
        PostResponse postResponse = postService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(postResponse);
    }

    @DeleteMapping("/post/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable("id") Long id) {
        postService.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping("/post/{id}")
    public ResponseEntity<PostResponse> updatePost(@PathVariable("id") Long id, @RequestBody @Valid SavePostRequest savePostRequest) {
        PostResponse postResponse = postService.updatePost(id, savePostRequest);
        return ResponseEntity.status(HttpStatus.OK).body(postResponse);
    }

    // 페이징과 검색
    @GetMapping("/paging/post")
    public ResponseEntity<Page<PostResponse>> list(@RequestParam(required = false) String title,
                                                   @RequestParam(required = false) String content, Pageable pageable) {
        SearchPostRequest searchPostRequest = new SearchPostRequest(title, content);
        Page<PostResponse> page = postService.searchAndPagePost(searchPostRequest, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(page);
    }
}
