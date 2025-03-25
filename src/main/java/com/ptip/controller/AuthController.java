package com.ptip.controller;

import com.ptip.entity.UserEntity;
import com.ptip.jwt.JWTUtil;
import com.ptip.models.dto.ResponseDto;
import com.ptip.models.dto.UserDto;
import com.ptip.repository.UserRepository;
import com.ptip.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final JWTUtil jwtUtil;
    private final TokenService tokenService;
    private final UserRepository userRepository;

    public AuthController(JWTUtil jwtUtil, TokenService tokenService, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.tokenService = tokenService;
        this.userRepository = userRepository;
    }

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

    @PostMapping("/reissue")
    public ResponseEntity<? super ResponseDto> reissue(
            HttpServletRequest request, HttpServletResponse response
    ){
        ResponseEntity<? super ResponseDto> tokenResponse = tokenService.reissueAccessToken(request, response);
        return tokenResponse;
    }

    @PostMapping("/logout")
    public ResponseEntity<? super ResponseDto> logout(
            HttpServletRequest request, HttpServletResponse response
    ){
        ResponseEntity<? super ResponseDto> tokenResponse = tokenService.removeRefreshToken(request, response);
        return tokenResponse;
    }
}
