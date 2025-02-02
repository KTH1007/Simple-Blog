package org.example.springbootdeveloper.User.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.springbootdeveloper.User.api.dto.request.LoginRequest;
import org.example.springbootdeveloper.User.api.dto.request.SignUpUserRequest;
import org.example.springbootdeveloper.User.api.dto.response.LoginResponse;
import org.example.springbootdeveloper.User.api.dto.response.SignUpUserResponse;
import org.example.springbootdeveloper.User.application.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    @Operation(summary = "회원 가입", description = "파라미터로 넘어온 정보로 회원 가입을 한다.")
    @ApiResponse(responseCode = "201", description = "성공")
    @ApiResponse(responseCode = "400", description = "파라미터 오류")
    @PostMapping("/signup")
    public ResponseEntity<SignUpUserResponse> signUp(@Parameter(description = "사용자 email과 password")
                                                     @RequestBody @Valid SignUpUserRequest signUpUserRequest) {
        SignUpUserResponse signUpUserResponse = userService.signUp(signUpUserRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(signUpUserResponse);
    }

    @Operation(summary = "로그인", description = "파라미터로 넘어온 정보로 로그인 한다.")
    @ApiResponse(responseCode = "201", description = "성공")
    @ApiResponse(responseCode = "400", description = "파라미터 오류")
    @ApiResponse(responseCode = "401", description = "인증 실패")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        LoginResponse loginResponse = userService.login(loginRequest);
        return ResponseEntity.ok(loginResponse);
    }


    // session 방식에서 사용하는 로그아웃 JWT에서는 변경
    @Operation(summary = "로그아웃", description = "로그아웃을 한다.")
    @ApiResponse(responseCode = "200", description = "성공")
    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        request.getSession().invalidate();
        return ResponseEntity.ok("logout successfully");
    }
}
