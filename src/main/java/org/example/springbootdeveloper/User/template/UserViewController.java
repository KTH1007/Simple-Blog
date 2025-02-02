package org.example.springbootdeveloper.User.template;

import lombok.RequiredArgsConstructor;
import org.example.springbootdeveloper.User.api.dto.request.SignUpUserRequest;
import org.example.springbootdeveloper.User.application.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class UserViewController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    // 로그인 페이지
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // 회원가입 페이지
    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }

    @PostMapping("/signup")
    public String processRegistration(SignUpUserRequest signUpUserRequest) {
        userService.signUp(signUpUserRequest);
        return "redirect:/login";
    }
}
