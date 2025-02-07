package org.example.springbootdeveloper.global.auditing;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

@Slf4j
public class AuditorAwareImpl implements AuditorAware<Long> {

    @Override
    public Optional<Long> getCurrentAuditor() {
        // SecurityContext에서 현재 사용자의 Autehntication 객체를 가져옴
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Authentication-> {}", authentication);
        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }

        // JWT에서 사용자 ID 추출
        Object principal = authentication.getPrincipal();
        log.info("principal -> {}", principal);

        if (principal instanceof CustomUserDetails) {
            return Optional.of(((CustomUserDetails) principal).getUserId());
        }

        return Optional.empty();
    }
}
