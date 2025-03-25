package com.ptip.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class KakaoOAuthService {

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    @Value("${kakao.token-uri}")
    private String tokenUri;

    @Value("${kakao.user-info-uri}")
    private String userInfoUri;

    @Value("${kakao.client-secret}")
    private String clientSecret;

    public String getAccessToken(String code) {
        System.out.println("카카오 인가코드: " + code);
        System.out.println("Client ID: " + clientId);
        System.out.println("Redirect URI: " + redirectUri);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);         // 필수
        body.add("redirect_uri", redirectUri);   // 필수
        body.add("code", code);                  // 필수
        body.add("client_secret", clientSecret);

        System.out.println("요청 바디: " + body);

        HttpEntity<?> request = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = new RestTemplate().postForEntity(tokenUri, request, Map.class);

        System.out.println("응답: " + response);

        return (String) response.getBody().get("access_token");
    }


    public Map<String, Object> getUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken); // Authorization: Bearer ~

        HttpEntity<?> request = new HttpEntity<>(headers);

        ResponseEntity<Map> response = new RestTemplate().exchange(
                userInfoUri,
                HttpMethod.GET,
                request,
                Map.class
        );

        return response.getBody();
    }
}

