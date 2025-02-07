package org.example.springbootdeveloper.User.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.springbootdeveloper.global.auditing.BaseTimeEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;

@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", updatable = false)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    private String password;
    private String nickname;
    private int age;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String provider;
    private String providerId;

//    @Builder
//    public User(String email, String password, String nickname, int age, Role role) {
//        this.email = email;
//        this.password = password;
//        this.nickname = nickname;
//        this.age = age;
//        this.role = role;
//    }

    @Builder
    public User(String email, String password, String nickname, int age, Role role, String provider, String providerId) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.age = age;
        this.role = role;
        this.provider = provider;
        this.providerId = providerId;
    }

    // 사용자 이름 변경
    public User update(String nickname) {
        this.nickname = nickname;
        return this;
    }

    // 권한 정보 반환
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(role.name()));
    }
}
