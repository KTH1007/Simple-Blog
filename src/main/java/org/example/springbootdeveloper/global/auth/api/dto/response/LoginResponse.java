package org.example.springbootdeveloper.global.auth.api.dto.response;

public record LoginResponse(
        String accessToken,
        String refreshToken
) {
    public LoginResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
