package org.example.springbootdeveloper.global.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Collections;

@RequiredArgsConstructor
public class JwtAuthenticationToken implements Authentication {

    private final String username;
    private boolean authenticated = true;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList(); // 권한 정보가 없으면 빈 리스트 반환
    }

    @Override
    public Object getCredentials() {
        return null; // 자격 증명 정보(비밀번호 등)는 필요 없음
    }

    @Override
    public Object getDetails() {
        return null; // 추가 정보는 필요 없음
    }

    @Override
    public Object getPrincipal() {
        return username; // 사용자 이름 반환
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated; // 인증 여부 반환
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        return username; // 사용자 이름 반환
    }
}
