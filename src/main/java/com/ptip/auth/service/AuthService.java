package com.ptip.auth.service;

import com.ptip.auth.jwt.JwtProvider;
import com.ptip.auth.models.User;
import com.ptip.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    public String kakaoLogin(Map<String, Object> kakaoUser) {
        String kakaoId = String.valueOf(kakaoUser.get("id"));

        Map<String, Object> properties = (Map<String, Object>) kakaoUser.get("properties");
        String nickname = (String) properties.get("nickname");
        String profileImage = (String) properties.get("profile_image");

        // 사용자 조회 또는 생성
        User user = userRepository.findById(kakaoId)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setId(kakaoId);
                    newUser.setNickname(nickname);
                    newUser.setProfileImage(profileImage);
                    return userRepository.save(newUser);
                });

        // JWT 토큰 발급
        return jwtProvider.createToken(user.getId());
    }
}

