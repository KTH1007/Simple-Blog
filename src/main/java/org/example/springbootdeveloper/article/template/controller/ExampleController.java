package org.example.springbootdeveloper.article.template.controller;

import org.example.springbootdeveloper.article.domain.Post;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;

@Controller
public class ExampleController {

    @GetMapping("/thymeleaf/example")
    public String thymeleafExample(Model model) {
        Post post = new Post("Sample title", "Sample content");

        model.addAttribute("post", post);
        model.addAttribute("today", LocalDateTime.now());
        return "example";
    }
}
