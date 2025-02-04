package org.example.springbootdeveloper.global.auth.application;

import lombok.RequiredArgsConstructor;
import org.example.springbootdeveloper.global.auth.domain.RefreshToken;
import org.example.springbootdeveloper.global.auth.domain.RefreshTokenRepository;
import org.example.springbootdeveloper.global.jwt.TokenProvider;
import org.example.springbootdeveloper.global.security.UserDetailService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenProvider tokenProvider;
    private final UserDetailService userDetailService;

    // RefreshToken 저장
    @Transactional
    public void saveRefreshToken(String username, String refreshToken) {
        RefreshToken token = RefreshToken.builder()
                .username(username)
                .refreshToken(refreshToken)
                .expiration(Instant.now().toEpochMilli() + tokenProvider.getRefreshTokenExpiration())
                .build();

        refreshTokenRepository.save(token);
    }

    // RefreshToken 조회
    public Optional<RefreshToken> findRefreshTokenByUsername(String username) {
        return refreshTokenRepository.findByUsername(username);
    }

    // RefreshToken 삭제
    @Transactional
    public void deleteRefreshTokenByUsername(String username) {
        refreshTokenRepository.deleteByUsername(username);
    }

    // RefreshToken 유효성 검사 및 AccessToken 재발급
    @Transactional
    public String reissueAccessToken(String refreshToken) {
        // RefreshToken 검증
        if (!tokenProvider.validateToken(refreshToken)) {
            throw new RuntimeException("Invalid Refresh Token");
        }

        // RefreshToken에서 사용자 이름 추출
        String username = tokenProvider.getUsernameFromToken(refreshToken);

        // DB에서 RefreshToken 조회
        RefreshToken storedToken = refreshTokenRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("RefreshToken not found"));

        // 저장된 RefreshToken과 요청된 RefreshToken 비교
        if (!storedToken.getRefreshToken().equals(refreshToken)) {
            throw new RuntimeException("RefreshToken mismatch");
        }

        // RefreshToken이 만료 되었는지 확인
        if (storedToken.getExpiration() < Instant.now().toEpochMilli()) {
            throw new RuntimeException("RefreshToken expired");
        }

        // role 가져오기
        UserDetails userDetails = userDetailService.loadUserByUsername(username);
        String role = userDetails.getAuthorities().stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Role not found"))
                .getAuthority();

        // 새로운 AccessToken 발급
        return tokenProvider.createAccessToken(username, role);
    }
}
