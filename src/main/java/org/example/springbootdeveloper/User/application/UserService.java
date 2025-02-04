package org.example.springbootdeveloper.User.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.springbootdeveloper.User.domain.Role;
import org.example.springbootdeveloper.User.domain.User;
import org.example.springbootdeveloper.User.domain.UserRepository;
import org.example.springbootdeveloper.User.exception.NotFoundUserException;
import org.example.springbootdeveloper.global.auth.api.dto.request.SignUpUserRequest;
import org.example.springbootdeveloper.global.auth.api.dto.response.SignUpUserResponse;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public SignUpUserResponse signUp(SignUpUserRequest signUpUserRequest) {
        if (userRepository.findByEmail(signUpUserRequest.email()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }
        if (userRepository.findByNickname(signUpUserRequest.nickname()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 닉네임입니다.");
        }

        User user = User.builder()
                .email(signUpUserRequest.email())
                .password(bCryptPasswordEncoder.encode(signUpUserRequest.password()))
                .nickname(signUpUserRequest.nickname())
                .age(signUpUserRequest.age())
                .role(Role.USER)
                .build();

        userRepository.save(user);

        return SignUpUserResponse.toDto(user);
    }

//    @Transactional
//    public LoginResponse login(LoginRequest loginRequest) {
//        log.debug("password -> {}", loginRequest.password());
//        User user = userRepository.findByEmail(loginRequest.email()).orElseThrow(NotFoundUserException::new);
//
//        if (!bCryptPasswordEncoder.matches(loginRequest.password(), user.getPassword())) {
//            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
//        }
//
//        return new LoginResponse(user.getEmail());
//    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(NotFoundUserException::new);
    }
}
