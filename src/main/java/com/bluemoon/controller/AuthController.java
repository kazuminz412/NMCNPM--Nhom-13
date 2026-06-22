package com.bluemoon.controller;

import com.bluemoon.dto.LoginRequest;
import com.bluemoon.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // 1. ĐĂNG NHẬP
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            java.util.Map<String, Object> responseData = authService.login(loginRequest.getUsername(), loginRequest.getPassword());
            // Trả về token và user info cho frontend lưu vào localStorage
            return ResponseEntity.ok(responseData);
        } catch (RuntimeException e) {
            // Trả về lỗi 401 nếu sai pass hoặc user (Frontend bắt biến message để in ra màn hình)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/hash")
    public String getHash() {
        return new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder().encode("123456");
    }

    // 2. ĐĂNG KÝ (Nếu làm tính năng tự đăng ký thì viết vào đây)
    /*
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        // Logic gọi sang AuthService để tạo tài khoản
        // ...
    }
    */
}
