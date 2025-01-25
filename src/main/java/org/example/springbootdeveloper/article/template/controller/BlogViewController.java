package org.example.springbootdeveloper.article.template.controller;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.example.springbootdeveloper.article.api.dto.request.SavePostRequest;
import org.example.springbootdeveloper.article.api.dto.request.SearchPostRequest;
import org.example.springbootdeveloper.article.api.dto.response.PostResponse;
import org.example.springbootdeveloper.article.application.PostService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class BlogViewController {

    private final PostService postService;

    @PostConstruct
    public void init() {
        SavePostRequest savePostRequest1 = new SavePostRequest("제목1", "내용1");
        postService.save(savePostRequest1);

        SavePostRequest savePostRequest2 = new SavePostRequest("제목1", "내용1");
        postService.save(savePostRequest2);

        SavePostRequest savePostRequest3 = new SavePostRequest("제목1", "내용1");
        postService.save(savePostRequest3);
    }

    @GetMapping("/post")
    public String getPost(Model model, SearchPostRequest searchPostRequest, Pageable pageable) {
        Page<PostResponse> postList = postService.searchAndPagePost(searchPostRequest, pageable);
        model.addAttribute("postList", postList.getContent()); // 데이터 리스트
        model.addAttribute("page", postList); // 페이징 정보
        return "postList";
    }

    @GetMapping("/post/{id}")
    public String getPostById(@PathVariable Long id, Model model) {
        PostResponse post = postService.findById(id);
        model.addAttribute("post", post);

        return "post";
    }

    @GetMapping("/form")
    public String newPost(@RequestParam(required = false) Long id, Model model) {
        if (id != null) {
            PostResponse post = postService.findById(id);
            model.addAttribute("post", post);
            model.addAttribute("postId", id);
        } else {
            model.addAttribute("post", new SavePostRequest("", ""));
            model.addAttribute("postId", null);
        }

        return "form";
    }
}
