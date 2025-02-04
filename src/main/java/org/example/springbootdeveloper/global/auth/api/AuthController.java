package org.example.springbootdeveloper.global.auth.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.springbootdeveloper.User.application.UserService;
import org.example.springbootdeveloper.global.auth.api.dto.request.LoginRequest;
import org.example.springbootdeveloper.global.auth.api.dto.request.ReissueRequest;
import org.example.springbootdeveloper.global.auth.api.dto.request.SignUpUserRequest;
import org.example.springbootdeveloper.global.auth.api.dto.response.LoginResponse;
import org.example.springbootdeveloper.global.auth.api.dto.response.ReissueResponse;
import org.example.springbootdeveloper.global.auth.api.dto.response.SignUpUserResponse;
import org.example.springbootdeveloper.global.auth.application.RefreshTokenService;
import org.example.springbootdeveloper.global.jwt.TokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;

    @Operation(summary = "회원 가입", description = "파라미터로 넘어온 정보로 회원 가입을 한다.")
    @ApiResponse(responseCode = "201", description = "생성")
    @ApiResponse(responseCode = "400", description = "파라미터 오류")
    @PostMapping("/signup")
    public ResponseEntity<SignUpUserResponse> signUp(@Parameter(description = "email, password, nickname, age")
                                                     @RequestBody @Valid SignUpUserRequest signUpUserRequest) {
        SignUpUserResponse signUpUserResponse = userService.signUp(signUpUserRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(signUpUserResponse);
    }

    @Operation(summary = "로그인", description = "파라미터로 넘어온 정보로 로그인 한다.")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "400", description = "파라미터 오류")
    @ApiResponse(responseCode = "401", description = "인증 실패")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest loginRequest) {
        try {        // 사용자 인증
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password())
            );

            // 인증된 사용자 정보 조회
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            // role 추출
            String role = userDetails.getAuthorities().stream()
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Role not found"))
                    .getAuthority();

            // JWT 토큰 생성
            String accessToken = tokenProvider.createAccessToken(userDetails.getUsername(), role);
            String refreshToken = tokenProvider.createRefreshToken(userDetails.getUsername());

            // RefreshToken 저장
            refreshTokenService.saveRefreshToken(userDetails.getUsername(), refreshToken);

            return ResponseEntity.status(HttpStatus.OK).body(new LoginResponse(accessToken, refreshToken));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }
    }

    @PostMapping("/reissue")
    public ResponseEntity<ReissueResponse> reissueAccessToken(@RequestBody ReissueRequest reissueRequest) {
        String newAccessToken = refreshTokenService.reissueAccessToken(reissueRequest.refreshToken());
        return ResponseEntity.status(HttpStatus.OK).body(new ReissueResponse(newAccessToken));
    }


    @Operation(summary = "로그아웃", description = "로그아웃을 한다.")
    @ApiResponse(responseCode = "200", description = "성공")
    @GetMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String refreshToken) {
        refreshToken = refreshToken.substring(7); // "Bearer" 제거
        String username = tokenProvider.getUsernameFromToken(refreshToken);

        // RefreshToken 삭제
        refreshTokenService.deleteRefreshTokenByUsername(username);

        return ResponseEntity.ok("Logout successfully");
    }

}
