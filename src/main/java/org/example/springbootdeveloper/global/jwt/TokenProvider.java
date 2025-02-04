package org.example.springbootdeveloper.global.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class TokenProvider {

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.access.expiration}")
    private long accessTokenExpiration;


    @Value("${jwt.refresh.expiration}")
    private long refreshTokenExpiration;

    // SecretKey 생성
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    // AccessToken 생성
    public String createAccessToken(String username, String role) {
        return createToken(username, role, accessTokenExpiration);
    }

    // RefreshToken 생성
    public String createRefreshToken(String username) {
        return createToken(username, "REFRESH", refreshTokenExpiration);
    }

    // Token 공통 생성 로직
    private String createToken(String username, String role, long expiration) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .subject(username) // 사용자 이름
                .claim("role", role)
                .issuedAt(now)
                .expiration(validity)
                .signWith(getSigningKey()) // 서명
                .compact();
    }

    // 토큰 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey()) // 서명 검증
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 토큰에서 사용자 이름 추출
    public String getUsernameFromToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public long getRefreshTokenExpiration() {
        return refreshTokenExpiration;
    }
}
