package com.ptip.auth.handler;

import com.ptip.auth.entity.RefreshTokenEntity;
import com.ptip.auth.jwt.JWTUtil;
import com.ptip.auth.dto.CustomOAuth2User;
import com.ptip.auth.repository.RefreshTokenRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JWTUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    public OAuth2SuccessHandler(JWTUtil jwtUtil, RefreshTokenRepository refreshTokenRepository) {
        this.jwtUtil = jwtUtil;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        //OAuth2User
        CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();
        String userId = customUserDetails.getUserId();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        String access = jwtUtil.createJwt("access", userId, role, 600000L);
        String refresh = jwtUtil.createJwt("refresh", userId, role, 86400000L);

        //Refresh 토큰 저장
        RefreshTokenEntity existData = refreshTokenRepository.findByUserId(userId);

        if (existData == null) {
            addRefreshTokenEntity(userId, refresh, 86400000L);
        } else {
            refreshTokenRepository.delete(existData);
            addRefreshTokenEntity(userId, refresh, 86400000L);
        }

        response.setHeader("Authorization", "Bearer " + access);
        response.addCookie(createCookie("refresh", refresh));
        response.setStatus(HttpStatus.OK.value());
        response.sendRedirect("https://www.ptutip.p-e.kr/signup");
    }

    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60);
        cookie.setSecure(true);  // https에서 사용
        cookie.setPath("/api/auth");
        cookie.setHttpOnly(true);

        return cookie;
    }

    private void addRefreshTokenEntity(String userId, String refreshToken, Long expiredMs) {

        Date currentDate = new Date(System.currentTimeMillis() + expiredMs);

        RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity();
        refreshTokenEntity.setUserId(userId);
        refreshTokenEntity.setToken(refreshToken);
        refreshTokenEntity.setExpirationData(currentDate.toString());

        refreshTokenRepository.save(refreshTokenEntity);
    }
}
