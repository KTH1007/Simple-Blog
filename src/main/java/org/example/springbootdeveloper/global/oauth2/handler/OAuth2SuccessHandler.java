package org.example.springbootdeveloper.global.oauth2.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.springbootdeveloper.global.auth.application.RefreshTokenService;
import org.example.springbootdeveloper.global.jwt.TokenProvider;
import org.example.springbootdeveloper.global.oauth2.domain.CustomOAuth2User;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

        String email = oAuth2User.getEmail();
        String role = oAuth2User.getRole();

        String accessToken = tokenProvider.createAccessToken(email, role);
        String refreshToken = tokenProvider.createRefreshToken(email);

        // RefreshToken 저장
        refreshTokenService.saveRefreshToken(email, refreshToken);

        // Token을 HttpOnly 쿠키에 저장
        addAccessTokenCookie(response, accessToken);
        addRefreshTokenCookie(response, refreshToken);

        // 응답에 토큰 추가
        response.setHeader("Authorization", "Bearer " + accessToken);
        response.setHeader("Refresh-Token", refreshToken);

        super.onAuthenticationSuccess(request, response, authentication);

        /*
        // 추가 정보 입력 페이지로 리다이렉트
        // 입력 폼을 제공해야 됨
        // controller도 작성 -> email과 nickname 추출 후 나머지 정보 기입
        response.sendRedirect("/additional-info");
         */
    }

    private void addAccessTokenCookie(HttpServletResponse response, String accessToken) {
        Cookie cookie = new Cookie("accessToken", accessToken);
        cookie.setHttpOnly(true); // JavaScript에서 접근 불가능
        cookie.setSecure(false); // true -> HTTPS 환경에서만 전송
        cookie.setPath("/"); // 모든 경로에서 접근 가능
        cookie.setMaxAge((int) tokenProvider.getAccessTokenExpiration() / 1000);
        response.addCookie(cookie);
    }

    private void addRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true); // JavaScript에서 접근 불가능
        cookie.setSecure(false); // true -> HTTPS 환경에서만 전송
        cookie.setPath("/"); // 모든 경로에서 접근 가능
        cookie.setMaxAge((int) tokenProvider.getRefreshTokenExpiration() / 1000);
        response.addCookie(cookie);
    }
}

