package com.bluemoon.service;

import com.bluemoon.model.NguoiDung;
import com.bluemoon.repository.NguoiDungRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private NguoiDungRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @Test
    void testCheckPassword_Success() {
        // 1. Chuẩn bị dữ liệu giả
        String rawPass = "mat_khau_that";
        String encodedPass = "hash_loang_ngoang";
        
        NguoiDung user = new NguoiDung();
        user.setUsername("admin");
        user.setPassword(encodedPass);

        // 2. Mock các hành vi
        when(repository.findByUsername("admin")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(rawPass, encodedPass)).thenReturn(true);

        // 3. Thực hiện kiểm tra (Ở đây ta kiểm tra logic bên trong authService.login)
        assertDoesNotThrow(() -> authService.login("admin", rawPass));
    }

    @Test
    void testCheckPassword_Fail() {
        // 1. Chuẩn bị dữ liệu
        String wrongPass = "sai_mat_khau";
        String encodedPass = "hash_loang_ngoang";
        
        NguoiDung user = new NguoiDung();
        user.setUsername("admin");
        user.setPassword(encodedPass);

        when(repository.findByUsername("admin")).thenReturn(Optional.of(user));
        // Mock passwordEncoder trả về false khi sai mật khẩu
        when(passwordEncoder.matches(wrongPass, encodedPass)).thenReturn(false);

        // 2. Kỳ vọng kết quả ném ra lỗi
        Exception exception = assertThrows(RuntimeException.class, () -> {
            authService.login("admin", wrongPass);
        });

        assertEquals("Sai tên đăng nhập hoặc mật khẩu!", exception.getMessage());
    }
}

