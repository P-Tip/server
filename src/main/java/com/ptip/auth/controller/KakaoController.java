package com.ptip.auth.controller;

import com.ptip.auth.service.AuthService;
import com.ptip.auth.service.KakaoOAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class KakaoController {

    private final KakaoOAuthService kakaoOAuthService;
    private final AuthService authService;

    @GetMapping("/oauth/kakao/callback")
    public ResponseEntity<?> kakaoCallback(@RequestParam String code) {
        String accessToken = kakaoOAuthService.getAccessToken(code);
        Map<String, Object> kakaoUser = kakaoOAuthService.getUserInfo(accessToken);

        String jwt = authService.kakaoLogin(kakaoUser);

        return ResponseEntity.ok().body(Map.of("token", jwt));
    }
}


