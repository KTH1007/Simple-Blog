package org.example.springbootdeveloper.article.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @Operation(summary = "게시글 작성", description = "파라미터로 넘어온 값으로 게시글을 작성한다.")
    @ApiResponse(responseCode = "201", description = "성공")
    @ApiResponse(responseCode = "400", description = "파라미터 오류")
    @PostMapping("/post")
    public ResponseEntity<PostResponse> savePost(@Parameter(description = "게시글의 제목과 내용 Dto") @RequestBody @Valid SavePostRequest savePostRequest) {
        PostResponse postResponse = postService.save(savePostRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(postResponse);
    }

    @Operation(summary = "게시글 리스트 조회", description = "모든 게시글을 조회한다.")
    @ApiResponse(responseCode = "201", description = "성공")
    @GetMapping("/posts")
    public ResponseEntity<List<PostResponse>> findAllPosts() {
        List<PostResponse> posts = postService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(posts);
    }

    @Operation(summary = "게시글 조회", description = "파라미터의 id값으로 하나의 게시글을 조회한다.")
    @ApiResponse(responseCode = "201", description = "성공")
    @ApiResponse(responseCode = "400", description = "파라미터 오류")
    @GetMapping("/post/{id}")
    public ResponseEntity<PostResponse> findPost(@PathVariable("id") Long id) {
        PostResponse postResponse = postService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(postResponse);
    }

    @Operation(summary = "게시글 삭제", description = "파라미터의 id값으로 하나의 게시글을 삭제한다.")
    @ApiResponse(responseCode = "201", description = "성공")
    @ApiResponse(responseCode = "400", description = "파라미터 오류")
    @DeleteMapping("/post/{id}")
    public ResponseEntity<Void> deletePost(@Parameter(description = "게시글 번호") @PathVariable("id") Long id) {
        postService.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "게시글 수정", description = "파리미터로 넘어온 id값과 정보로 게시글을 수정한다.")
    @ApiResponse(responseCode = "201", description = "성공")
    @ApiResponse(responseCode = "400", description = "파라미터 오류")
    @PatchMapping("/post/{id}")
    public ResponseEntity<PostResponse> updatePost(@Parameter(description = "게시글 번호") @PathVariable("id") Long id,
                                                   @Parameter(description = "게시글의 제목과 내용 Dto") @RequestBody @Valid SavePostRequest savePostRequest) {
        PostResponse postResponse = postService.updatePost(id, savePostRequest);
        return ResponseEntity.status(HttpStatus.OK).body(postResponse);
    }

    // 페이징과 검색
    @Operation(summary = "게시글 페이징 리스트", description = "페이징 처리와 검색이 가능한 게시글 목록을 반환한다.")
    @ApiResponse(responseCode = "201", description = "성공")
    @ApiResponse(responseCode = "400", description = "파라미터 오류")
    @GetMapping("/paging/post")
    public ResponseEntity<Page<PostResponse>> list(@Parameter(description = "게시글의 제목")@RequestParam(required = false) String title,
                                                   @Parameter(description = "게시글의 내용")@RequestParam(required = false) String content,
                                                   Pageable pageable) {
        SearchPostRequest searchPostRequest = new SearchPostRequest(title, content);
        Page<PostResponse> page = postService.searchAndPagePost(searchPostRequest, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(page);
    }
}
