package org.example.springbootdeveloper.global.oauth2.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.springbootdeveloper.User.domain.Role;
import org.example.springbootdeveloper.User.domain.User;
import org.example.springbootdeveloper.User.domain.UserRepository;
import org.example.springbootdeveloper.global.oauth2.domain.CustomOAuth2User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId();

        Map<String, Object> attributes = oAuth2User.getAttributes();

        // 네이버인 경우 응답 데이터가 "response" 키로 감싸져 있음
        if (provider.equals("naver")) {
            attributes = (Map<String, Object>) attributes.get("response");
        }

        String providerId;

        if (provider.equals("google")) {
            providerId = (String) attributes.get("sub"); // Google의 사용자 ID는 "sub"
        } else if (provider.equals("naver")) {
            providerId = (String) attributes.get("id"); // Naver의 사용자 ID는 "id"
        } else {
            throw new IllegalArgumentException("지원하지 않는 OAuth2 제공자입니다: " + provider);
        }

        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");

        log.info("email -> {}", email);

        User user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    User newUser = User.builder()
                            .email(email)
                            .password(passwordEncoder.encode("oauth2user")) // 기본 비밀번호 설정 (사용되지 않음)
                            .nickname(name)
                            .age(0) // 기본값
                            .provider(provider)
                            .providerId(providerId)
                            .role(Role.USER)
                            .build();
                    return userRepository.save(newUser);
                });

        return new CustomOAuth2User(user, oAuth2User.getAttributes());
    }
}
