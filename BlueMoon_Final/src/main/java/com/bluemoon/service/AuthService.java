package com.bluemoon.service;

import com.bluemoon.model.NguoiDung; 
import com.bluemoon.repository.NguoiDungRepository;
import com.bluemoon.security.JwtUtils; 
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final NguoiDungRepository repository; 
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    // 1. LOGIN
    public java.util.Map<String, Object> login(String username, String password) {
        // 1. Tìm Người dùng theo username
        NguoiDung user = repository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("Sai tên đăng nhập hoặc mật khẩu!")); // Lỗi 401
        
        boolean matches = passwordEncoder.matches(password, user.getPassword());

        if (!matches) {
            throw new RuntimeException("Sai tên đăng nhập hoặc mật khẩu!"); // Lỗi 401
        }
        
        // 3. Nếu đúng, trả về Token (truyền đủ 3 tham số cho JwtUtils)
        String token = jwtUtils.generateToken(user.getUsername(), user.getRole(), user.getHoDanId());
        
        java.util.Map<String, Object> result = new java.util.HashMap<>();
        result.put("token", token);
        result.put("user", user);
        return result;
    }

    // 2. REGISTER
    public void register(NguoiDung nguoiDung) {
        if (repository.existsByUsername(nguoiDung.getUsername())) {
            throw new RuntimeException("Tên đăng nhập này đã có người sử dụng!");
        }

        // Mã hóa mật khẩu
        nguoiDung.setPassword(passwordEncoder.encode(nguoiDung.getPassword()));
        
        // Gán quyền mặc định
        if (nguoiDung.getRole() == null || nguoiDung.getRole().isEmpty()) {
            nguoiDung.setRole("CU_DAN"); 
        }

        repository.save(nguoiDung);
    }
}
