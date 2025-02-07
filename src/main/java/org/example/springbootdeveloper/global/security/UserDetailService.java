package org.example.springbootdeveloper.global.security;

import lombok.RequiredArgsConstructor;
import org.example.springbootdeveloper.User.domain.User;
import org.example.springbootdeveloper.User.domain.UserRepository;
import org.example.springbootdeveloper.User.exception.NotFoundUserException;
import org.example.springbootdeveloper.global.auditing.CustomUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(NotFoundUserException::new);
        return new CustomUserDetails(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                user.getAuthorities()
        );
    }
}
