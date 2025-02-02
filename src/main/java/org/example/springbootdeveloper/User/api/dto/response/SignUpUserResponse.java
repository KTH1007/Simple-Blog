package org.example.springbootdeveloper.User.api.dto.response;

import lombok.Builder;
import org.example.springbootdeveloper.User.domain.User;

import java.time.LocalDateTime;

@Builder
public record SignUpUserResponse(
        Long id,
        String email,
        String nickname,
        int age,
        LocalDateTime createdAt,
        LocalDateTime lastModifiedAt
) {
    public static SignUpUserResponse toDto(User user) {
        return SignUpUserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .age(user.getAge())
                .createdAt(user.getCreatedAt())
                .lastModifiedAt(user.getLastModifiedAt())
                .build();
    }
}
