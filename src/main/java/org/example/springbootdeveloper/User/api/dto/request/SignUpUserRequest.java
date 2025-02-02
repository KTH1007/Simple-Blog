package org.example.springbootdeveloper.User.api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import org.example.springbootdeveloper.User.domain.Role;
import org.example.springbootdeveloper.User.domain.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public record SignUpUserRequest(
        @Email
        String email,
        @NotEmpty
        String password,
        @NotEmpty
        String nickname,
        @NotEmpty
        int age
) {
    public User toEntity(BCryptPasswordEncoder bCryptPasswordEncoder) {
        return User.builder()
                .email(email)
                .password(bCryptPasswordEncoder.encode(password))
                .nickname(nickname)
                .age(age)
                .role(Role.USER)
                .build();
    }
}
