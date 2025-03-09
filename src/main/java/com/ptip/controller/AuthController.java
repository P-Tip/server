package com.ptip.controller;

import com.ptip.mapper.UserMapper;
import com.ptip.models.User;
import com.ptip.util.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserMapper userMapper, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public String register(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("USER");
        userMapper.saveUser(user);
        return "User registered successfully";
    }

    @PostMapping("/login")
    public String login(@RequestBody User user) {
        User existingUser = userMapper.findByEmail(user.getEmail());

        if (existingUser == null || !passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
            return "Invalid email or password";
        }

        return jwtUtil.generateToken(existingUser.getEmail());
    }
}