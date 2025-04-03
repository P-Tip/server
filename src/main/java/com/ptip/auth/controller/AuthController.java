package com.ptip.auth.controller;

import com.ptip.auth.entity.UserEntity;
import com.ptip.auth.jwt.JWTUtil;
import com.ptip.auth.dto.ResponseDto;
import com.ptip.auth.dto.UserDto;
import com.ptip.auth.repository.UserRepository;
import com.ptip.auth.service.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authorization", description = "로그인 관련 API")
public class AuthController {
    private final JWTUtil jwtUtil;
    private final TokenService tokenService;
    private final UserRepository userRepository;

    public AuthController(JWTUtil jwtUtil, TokenService tokenService, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.tokenService = tokenService;
        this.userRepository = userRepository;
    }

    @Operation(summary = "토큰 확인", description = "액세스 토큰을 헤더에 담아서 요청을 보내면 해당 사용자의 토큰 상태를 확인해줍니다. (로그인 실행 X)")
    @GetMapping("/check")
    public ResponseEntity<? super ResponseDto> check(
            @RequestHeader("Authorization") String authorization
    ) {
        String token = authorization.substring(7);

        if (jwtUtil.isExpired(token)) {
            ResponseEntity<? super ResponseDto> response = ResponseDto.validationFail();
            return response;
        }

        String userId = jwtUtil.getUserId(token);
        UserEntity existData = userRepository.findByUserId(userId);

        UserDto userDto = new UserDto(userId, existData.getName(), existData.getRole());
        ResponseEntity<? super ResponseDto> response = ResponseDto.success("Login successful.", userDto);
        return response;
    }

    @Operation(summary = "액세스 토큰 재발급", description = "쿠키에 리프레시 토큰을 담아서 요청을 보내면 해당 사용자의 액세스 토큰과 리프레시 토큰을 각각 헤더와 쿠키로 재발급해줍니다.")
    @PostMapping("/reissue")
    public ResponseEntity<? super ResponseDto> reissue(
            HttpServletRequest request, HttpServletResponse response
    ){
        ResponseEntity<? super ResponseDto> tokenResponse = tokenService.reissueAccessToken(request, response);
        return tokenResponse;
    }

    @Operation(summary = "리프레시 토큰 삭제", description = "쿠키에 리프레시 토큰을 담아서 요청을 보내면 해당 사용자의 리프레시 토큰을 DB에서 삭제해줍니다.")
    @PostMapping("/logout")
    public ResponseEntity<? super ResponseDto> logout(
            HttpServletRequest request, HttpServletResponse response
    ){
        ResponseEntity<? super ResponseDto> tokenResponse = tokenService.removeRefreshToken(request, response);
        return tokenResponse;
    }
}
