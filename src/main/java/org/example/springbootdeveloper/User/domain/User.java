package org.example.springbootdeveloper.User.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.springbootdeveloper.global.auditing.BaseTimeEntity;

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

    @Builder
    public User(String email, String password, String nickname, int age, Role role) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.age = age;
        this.role = role;
    }
}
