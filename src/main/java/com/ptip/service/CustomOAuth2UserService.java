package com.ptip.service;

import com.ptip.entity.UserEntity;
import com.ptip.models.CustomOAuth2User;
import com.ptip.models.GoogleResponse;
import com.ptip.models.dto.UserDto;
import com.ptip.repository.UserRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private static final String ALLOWED_EMAIL_DOMAIN = "@ptu.ac.kr"; // 허용된 이메일 도메인

    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        System.out.println(oAuth2User);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        GoogleResponse googleResponse = null;

        if (registrationId.equals("naver")) {

            return null;
        }
        else if (registrationId.equals("google")) {

            googleResponse = new GoogleResponse(oAuth2User.getAttributes());
        }
        else {

            return null;
        }

        String email = googleResponse.getEmail();
        if (email != null && !email.endsWith(ALLOWED_EMAIL_DOMAIN)) {
            throw new OAuth2AuthenticationException("Invalid email domain");  // 도메인이 맞지 않으면 예외 발생
        }

        //리소스 서버에서 발급 받은 정보로 사용자를 특정할 아이디값을 만듬
        String userId = googleResponse.getProvider()+"_"+googleResponse.getProviderId();
        UserEntity existData = userRepository.findByUserId(userId);

        if (existData == null) {

            UserEntity userEntity = new UserEntity();
            userEntity.setUserId(userId);
            userEntity.setEmail(googleResponse.getEmail());
            userEntity.setName(googleResponse.getName());
            userEntity.setRole("ROLE_USER");
            userEntity.setProvider(googleResponse.getProvider());

            userRepository.save(userEntity);

            UserDto userDTO = new UserDto();
            userDTO.setUserId(userId);
            userDTO.setName(googleResponse.getName());
            userDTO.setRole("ROLE_USER");

            return new CustomOAuth2User(userDTO);
        }
        else {

            existData.setEmail(googleResponse.getEmail());
            existData.setName(googleResponse.getName());
            existData.setProvider(googleResponse.getProvider());

            userRepository.save(existData);

            UserDto userDTO = new UserDto();
            userDTO.setUserId(existData.getUserId());
            userDTO.setName(googleResponse.getName());
            userDTO.setRole(existData.getRole());

            return new CustomOAuth2User(userDTO);
        }
    }
}
