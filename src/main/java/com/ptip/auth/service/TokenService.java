package com.ptip.auth.service;

import com.ptip.auth.entity.RefreshTokenEntity;
import com.ptip.auth.jwt.JWTUtil;
import com.ptip.auth.dto.ResponseDto;
import com.ptip.auth.repository.RefreshTokenRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TokenService {
    private final JWTUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    public TokenService(JWTUtil jwtUtil, RefreshTokenRepository refreshTokenRepository) {
        this.jwtUtil = jwtUtil;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    //get refresh token
    public ResponseEntity<? super ResponseDto> reissueAccessToken(HttpServletRequest request, HttpServletResponse response) {

        String refreshToken = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {

            if (cookie.getName().equals("refresh")) {

                refreshToken = cookie.getValue();
            }
        }

        if (refreshToken == null) {
            return ResponseDto.invalidToken("Refresh token is missing.");
        }
        //expired check
        if (jwtUtil.isExpired(refreshToken)) {
            return ResponseDto.invalidToken("Refresh token expired.");
        }


        // 토큰이 refresh인지 확인 (발급시 페이로드에 명시)
        String category = jwtUtil.getCategory(refreshToken);

        if (!category.equals("refresh")) {

            //response status code
            return ResponseDto.invalidToken("Invalid refresh token.");
        }

        //DB에 저장되어 있는지 확인
        Boolean isExist = refreshTokenRepository.existsByToken(refreshToken);
        if (!isExist) {

            //response body
            return ResponseDto.invalidToken("Invalid refresh token in DB");
        }

        String userId = jwtUtil.getUserId(refreshToken);
        String role = jwtUtil.getRole(refreshToken);

        //make new JWT
        String newAccess = jwtUtil.createJwt("access", userId, role, 600000L);
        String newRefresh = jwtUtil.createJwt("refresh", userId, role, 86400000L);

        //Refresh 토큰 저장 DB에 기존의 Refresh 토큰 삭제 후 새 Refresh 토큰 저장
        refreshTokenRepository.deleteByToken(refreshToken);
        addRefreshTokenEntity(userId, newRefresh, 86400000L);

        //response
        response.setHeader("access", newAccess);
        response.addCookie(createCookie("refresh", newRefresh));
        return ResponseDto.success("New access token generated.", null);
    }

    public ResponseEntity<? super ResponseDto> removeRefreshToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {

            if (cookie.getName().equals("refresh")) {

                refreshToken = cookie.getValue();
            }
        }

        if (refreshToken == null) {
            return ResponseDto.invalidToken("Refresh token is missing.");
        }
        //expired check
        if (jwtUtil.isExpired(refreshToken)) {
            return ResponseDto.invalidToken("Refresh token expired.");
        }

        // 토큰이 refresh인지 확인 (발급시 페이로드에 명시)
        String category = jwtUtil.getCategory(refreshToken);
        if (!category.equals("refresh")) {

            return ResponseDto.invalidToken("Invalid refresh token.");
        }

        //DB에 저장되어 있는지 확인
        Boolean isExist = refreshTokenRepository.existsByToken(refreshToken);
        if (!isExist) {

            return ResponseDto.invalidToken("Invalid refresh token in DB.");
        }

        //로그아웃 진행
        //Refresh 토큰 DB에서 제거
        refreshTokenRepository.deleteByToken(refreshToken);

        //Refresh 토큰 Cookie 값 0
        Cookie cookie = new Cookie("refresh", null);
        cookie.setMaxAge(0);
        //cookie.setPath("/");
//        cookie.setHttpOnly(true);

        response.addCookie(cookie);
        return ResponseDto.success("Logout successful.", null);
    }

    //extractToken

    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60);
        cookie.setSecure(true);  // https에서 사용
        //cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }

    private void addRefreshTokenEntity(String userId, String refreshToken, Long expiredMs) {

        Date date = new Date(System.currentTimeMillis() + expiredMs);

        RefreshTokenEntity refreshEntity = new RefreshTokenEntity();
        refreshEntity.setUserId(userId);
        refreshEntity.setToken(refreshToken);
        refreshEntity.setExpirationData(date.toString());

        refreshTokenRepository.save(refreshEntity);
    }
}
