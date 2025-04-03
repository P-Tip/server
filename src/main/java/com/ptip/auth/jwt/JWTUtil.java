package com.ptip.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;


@Component
public class JWTUtil {
    private final SecretKey secretKey;

    public JWTUtil(@Value("${spring.jwt.secret}") String secret) {  // 속성 가져오는 어노테이션
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());  // 암호화 방식 HS256으로 설정
    }

    public String getType(String token) {
        return parseToken(token).get("type", String.class);
    }

    public String getUserId(String token) {  // username 학인하기 위해
        return parseToken(token).get("user_id", String.class);
    }

    public  String getName(String token) {
        return parseToken(token).get("name", String.class);
    }

    public String getRole(String token) {  // role 학인하기 위해
        return parseToken(token).get("role", String.class);
    }

    public boolean isExpired(String token) {
        try {
            return parseToken(token).getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        }
    }

    private Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String createJwt(String type, String userId, String name, String role, Long expiredMs) {  // 토큰 생성

        return Jwts.builder()
                .header().add("typ", "JWT")
                .and()
                .claim("type", type)
                .claim("user_id", userId)
                .claim("name", name)
                .claim("role", role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();
    }
}
