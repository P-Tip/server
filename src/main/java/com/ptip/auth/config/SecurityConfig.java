package com.ptip.auth.config;

import com.ptip.auth.handler.OAuth2FailureHandler;
import com.ptip.auth.handler.OAuth2SuccessHandler;
import com.ptip.auth.jwt.JWTFilter;
import com.ptip.auth.jwt.JWTUtil;
import com.ptip.auth.service.CustomOAuth2UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.io.IOException;

@Configurable
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final OAuth2FailureHandler oAuth2FailureHandler;
    private final JWTUtil jwtUtil;

    public SecurityConfig(CustomOAuth2UserService customOAuth2UserService, OAuth2SuccessHandler oAuth2SuccessHandler, OAuth2FailureHandler oAuth2FailureHandler, JWTUtil jwtUtil) {
        this.customOAuth2UserService = customOAuth2UserService;
        this.oAuth2SuccessHandler = oAuth2SuccessHandler;
        this.oAuth2FailureHandler = oAuth2FailureHandler;
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .cors(corsCustomizer -> corsCustomizer
                        .configurationSource(corsConfigurationSource()))

                .csrf((auth) -> auth.disable())             // csrf disable csrf공격에 대한 방어를 하기 위해 사용
                .formLogin((auth) -> auth.disable())   // From 로그인 방식 disable
                .httpBasic((auth) -> auth.disable())   // HTTP Basic 인증 방식 disable

                .addFilterBefore(new JWTFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class)  // JWTFilter 추가

                .oauth2Login((oauth2) -> oauth2
                        .authorizationEndpoint(endpoint -> endpoint.baseUri("/api/auth/oauth2"))  //요청경로 변경 http://localhost:8080/api/auth/oauth2/google
                        .redirectionEndpoint(endpoint -> endpoint.baseUri("/oauth2/callback/*"))  // /oauth2/callback/*
                        .userInfoEndpoint((userInfoEndpointConfig) -> userInfoEndpointConfig
                                .userService(customOAuth2UserService))
                        .successHandler(oAuth2SuccessHandler)
                        .failureHandler(oAuth2FailureHandler)
                )

                .authorizeHttpRequests((auth) -> auth   // 경로별 인가 작업
                        .requestMatchers("/", "/api/auth/**", "api/award/**").permitAll()
                        .requestMatchers("/api/admin").hasRole("ROLE_ADMIN")
                )

        //세션 설정 : STATELESS
//        http
//                .sessionManagement((session) -> session
//                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

                .exceptionHandling(exceptionHandle -> exceptionHandle
                        .authenticationEntryPoint(new FailedAuthenticationEntryPoint())
                );

        return http.build();
    }

    @Bean
    protected CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration CorsConfiguration = new CorsConfiguration();
        CorsConfiguration.addAllowedOrigin("*");
        CorsConfiguration.addAllowedMethod("*");
        CorsConfiguration.addAllowedHeader("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", CorsConfiguration);

        return source;
    }
}

class FailedAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN); // {"code": "NP", "message": "No Permission."}
        response.getWriter().write("{\"code\": \"FORBIDDEN\", \"message\": \"No permission to access.\"}");
    }
}
