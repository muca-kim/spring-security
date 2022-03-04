package com.project.security.controller;

import java.util.Map;

import com.project.security.model.User;
import com.project.security.service.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/signup")
    public ResponseEntity<HttpStatus> signup(@RequestBody Map<String, String> user) throws Exception {
        log.info("signup");
        log.info(user.get("userId"));
        log.info("instance of BCrypt={}", passwordEncoder instanceof BCryptPasswordEncoder);
        String encodedPassword = passwordEncoder.encode(user.get("password"));
        User userEn = User.builder()
                .userId(user.get("userId"))
                .password(encodedPassword)
                .name(user.get("name"))
                .address(user.get("address"))
                .role(user.get("role"))
                .phoneNo(user.get("phoneNo")).build();
        userService.addUser(userEn);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
