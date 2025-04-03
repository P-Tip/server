package com.ptip.auth.jwt;

import com.ptip.auth.dto.CustomOAuth2User;
import com.ptip.auth.dto.UserDto;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JWTFilter extends OncePerRequestFilter {

    private  final JWTUtil jwtUtil;
    private static final int BEARER_PREFIX_LENGTH = "Bearer ".length();

    public JWTFilter(JWTUtil jwtUtil){
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //request에서 Authorization 헤더를 찾음
        String accessToken = request.getHeader("Authorization");

        //Authorization 헤더 검증
        if (accessToken == null || !accessToken.startsWith("Bearer ")) {

//            System.out.println("access token null");
            filterChain.doFilter(request, response);

            //조건이 해당되면 메소드 종료 (필수)
            return;
        }

        //Bearer 부분 제거 후 순수 토큰만 획득
        String token = accessToken.substring(BEARER_PREFIX_LENGTH);

        try {
            //토큰 소멸 시간 검증
            if (jwtUtil.isExpired(token)) {
//            System.out.println("token expired");
                setErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED,
                        "EXPIRED_TOKEN", "토큰이 만료되었습니다.");
                return;
            }

            // access 토큰인지 확인
            String category = jwtUtil.getType(token);
            if (!category.equals("access")) {
                setErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED,
                        "INVALID_TOKEN_TYPE", "Access 토큰이 아닙니다.");
                return;
            }

            //토큰에서 username과 role 획득
            String userId = jwtUtil.getUserId(token);
            String role = jwtUtil.getRole(token);

            //userDTO를 생성하여 값 set
            UserDto userDto = new UserDto();
            userDto.setUserId(userId);
            userDto.setRole(role);

            //UserDetails에 회원 정보 객체 담기
            CustomOAuth2User customOAuth2User = new CustomOAuth2User(userDto);

            //스프링 시큐리티 인증 토큰 생성
            Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.getAuthorities());
            //세션에 사용자 등록
            SecurityContextHolder.getContext().setAuthentication(authToken);

            filterChain.doFilter(request, response);

        } catch (JwtException e) {
            // 토큰 형식 오류, 서명 오류, 변조된 토큰 등
            setErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED,
                    "INVALID_TOKEN", "유효하지 않은 토큰입니다.");
        }
    }

    private void setErrorResponse(HttpServletResponse response, int status, String errorCode, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json;charset=UTF-8");
        String responseBody = String.format("{\"error\": \"%s\", \"message\": \"%s\"}", errorCode, message);
        response.getWriter().write(responseBody);
    }
}
