package org.example.springbootdeveloper.global.auth.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username; // 사용자 식별자

    @Column(nullable = false)
    private String refreshToken;

    @Column(nullable = false)
    private Long expiration;


    @Builder
    public RefreshToken(String username, String refreshToken, long expiration) {
        this.username = username;
        this.refreshToken = refreshToken;
        this.expiration = expiration;
    }

    private void updateToken(String refreshToken) {
        this.refreshToken = refreshToken;
        this.expiration = expiration;
    }
}
