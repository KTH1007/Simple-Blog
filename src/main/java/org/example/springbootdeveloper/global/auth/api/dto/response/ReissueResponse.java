package org.example.springbootdeveloper.global.auth.api.dto.response;

public record ReissueResponse(
        String accessToken
) {
    public ReissueResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}
