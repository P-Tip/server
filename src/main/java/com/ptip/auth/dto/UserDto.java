package com.ptip.auth.dto;

import org.springframework.stereotype.Component;

@Component
public class UserDto {  // API를 만들기 위해
    private String userId;
    private String name;
    private String role;

    public UserDto() {}

    public UserDto(String userId, String name, String role) {
        this.userId = userId;
        this.name = name;
        this.role = role;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

}
