package org.example.springbootdeveloper.article.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.springbootdeveloper.article.api.dto.request.SavePostRequest;
import org.example.springbootdeveloper.article.api.dto.response.PostResponse;
import org.example.springbootdeveloper.article.application.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc // MockMvc 생성 및 자동 구성
class PostControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper; // 직렬화, 역직렬화를 위한 클래스

    @Autowired
    protected WebApplicationContext context;

    @Autowired
    PostService postService;

    @BeforeEach
    public void mockMvcSetup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .build();
        postService.deleteAll();
    }

    @Test
    @DisplayName("블로그에 글 추가")
    public void addPost() throws Exception {
        // given
        final String url = "/api/post";
        final String title = "title";
        final String content = "content";
        final SavePostRequest savePostRequest = new SavePostRequest(title, content);

        // Json으로 직렬화
        final String requestBody = objectMapper.writeValueAsString(savePostRequest);

        // when
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody));

        // then
        result.andExpect(MockMvcResultMatchers.status().isCreated());

        List<PostResponse> posts = postService.findAll();

        assertThat(posts.size()).isEqualTo(1);
        assertThat(posts.get(0).title()).isEqualTo(title);
        assertThat(posts.get(0).content()).isEqualTo(content);
    }

    @Test
    @DisplayName("글 목록 조회")
    public void findAllPosts() throws Exception {
        // given
        final String url = "/api/posts";
        final String title = "title";
        final String content = "content";

        SavePostRequest savePostRequest = new SavePostRequest(title, content);
        postService.save(savePostRequest);

        // when
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get(url)
                .accept(MediaType.APPLICATION_JSON_VALUE));

        // then
        result
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value(title))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].content").value(content));
    }

    @Test
    @DisplayName("글 조회")
    public void findPost() throws Exception {
        // given
        final String url = "/api/post/{id}";
        final String title = "title";
        final String content = "content";

        PostResponse postResponse = postService.save(new SavePostRequest(title, content));

        // when
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get(url, postResponse.id()));

        // then
        result
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(title))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").value(content));
    }

    @Test
    @DisplayName("글 삭제")
    public void deletePost() throws Exception {
        // given
        final String url = "/api/post/{id}";
        final String title = "title";
        final String content = "content";

        PostResponse postResponse = postService.save(new SavePostRequest(title, content));

        // when
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.delete(url, postResponse.id()));

        // then
        result.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("글 수정")
    public void updatePost() throws Exception {
        // given
        final String url = "/api/post/{id}";
        final String title = "title";
        final String content = "content";

        PostResponse postResponse = postService.save(new SavePostRequest(title, content));

        final String newTitle = "newTitle";
        final String newContent = "newContent";

        SavePostRequest savePostRequest = new SavePostRequest(newTitle, newContent);

        // when
        PostResponse postResponse2 = postService.updatePost(postResponse.id(), savePostRequest);

        // then
        assertThat(postResponse2.title()).isEqualTo(newTitle);
        assertThat(postResponse2.content()).isEqualTo(newContent);
    }
}
