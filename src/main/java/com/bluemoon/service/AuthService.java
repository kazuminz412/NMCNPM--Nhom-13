package com.bluemoon.service;

import com.bluemoon.model.NguoiDung;
import com.bluemoon.repository.NguoiDungRepository;
import com.bluemoon.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner; // Thư viện để tự chạy code
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService implements CommandLineRunner {

    private final NguoiDungRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    // 🔥 HÀM NÀY SẼ TỰ ĐỘNG CHẠY 1 LẦN KHI START SERVER ĐỂ SỬA LỖI MẬT KHẨU
    @Override
    public void run(String... args) throws Exception {
        repository.findByUsername("admin").ifPresent(admin -> {
            admin.setPassword(passwordEncoder.encode("123456"));
            repository.save(admin);
            System.out.println("✅ ĐÃ RESET MẬT KHẨU ADMIN VỀ '123456' CHUẨN 100%!");
        });
    }

    // 1. LOGIN
    public String login(String username, String password) {
        NguoiDung user = repository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Sai tên đăng nhập hoặc mật khẩu!"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Sai tên đăng nhập hoặc mật khẩu!");
        }

        return jwtUtils.generateToken(user.getUsername(), user.getRole(), user.getHoDanId());
    }

    // 2. REGISTER
    public void register(NguoiDung nguoiDung) {
        if (repository.existsByUsername(nguoiDung.getUsername())) {
            throw new RuntimeException("Tên đăng nhập này đã có người sử dụng!");
        }

        nguoiDung.setPassword(passwordEncoder.encode(nguoiDung.getPassword()));

        if (nguoiDung.getRole() == null || nguoiDung.getRole().isEmpty()) {
            nguoiDung.setRole("CU_DAN");
        }

        repository.save(nguoiDung);
    }
}