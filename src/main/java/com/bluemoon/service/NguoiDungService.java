package com.bluemoon.service;

import com.bluemoon.model.NguoiDung;
import com.bluemoon.repository.NguoiDungRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NguoiDungService {
    
    private final NguoiDungRepository nguoiDungRepository;
    private final PasswordEncoder passwordEncoder;

    // ==========================================
    // 0. HÀM ĐĂNG NHẬP (Dùng cho Controller)
    // ==========================================
    public NguoiDung checkLogin(String username, String password) {
        Optional<NguoiDung> userOpt = nguoiDungRepository.findByUsername(username);
        
        if (userOpt.isPresent()) {
            String dbPassword = userOpt.get().getPassword();
            // Mẹo cho Sprint 1: Chấp nhận cả mật khẩu mã hóa (matches) HOẶC mật khẩu text thường (equals) trong file SQL
            if (passwordEncoder.matches(password, dbPassword) || dbPassword.equals(password)) {
                return userOpt.get();
            }
        }
        return null;
    }

    // ==========================================
    // CÁC HÀM CRUD CƠ BẢN
    // ==========================================

    // 1. Lấy danh sách
    public List<NguoiDung> findAll() {
        return nguoiDungRepository.findAll();
    }

    // 2. Thêm mới (Đổi tên register thành save cho khớp Controller)
    public NguoiDung save(NguoiDung nguoiDung) {
        if (nguoiDungRepository.existsByUsername(nguoiDung.getUsername())) {
            throw new RuntimeException("Tên đăng nhập đã tồn tại!");
        }
        // Mã hóa mật khẩu trước khi lưu xuống DB
        nguoiDung.setPassword(passwordEncoder.encode(nguoiDung.getPassword()));
        return nguoiDungRepository.save(nguoiDung);
    }

    // 3. Cập nhật
    public NguoiDung update(Long id, NguoiDung nguoiDungDetails) {
        NguoiDung existingUser = nguoiDungRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản!"));

        existingUser.setRole(nguoiDungDetails.getRole());
        existingUser.setNhanKhauId(nguoiDungDetails.getNhanKhauId());
        if (nguoiDungDetails.getHoTen() != null) {
            existingUser.setHoTen(nguoiDungDetails.getHoTen());
        }
        if (nguoiDungDetails.getHoDanId() != null) {
            existingUser.setHoDanId(nguoiDungDetails.getHoDanId());
        }
        if (nguoiDungDetails.getTrangThai() != null) {
            existingUser.setTrangThai(nguoiDungDetails.getTrangThai());
        }
        
        // Chỉ cập nhật mật khẩu nếu Frontend có gửi mật khẩu mới lên
        if (nguoiDungDetails.getPassword() != null && !nguoiDungDetails.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(nguoiDungDetails.getPassword()));
        }

        return nguoiDungRepository.save(existingUser);
    }

    // 4. Xóa
    public void delete(Long id) {
        nguoiDungRepository.deleteById(id);
    }

    // Hàm hỗ trợ tìm kiếm
    public Optional<NguoiDung> findByUsername(String username) {
        return nguoiDungRepository.findByUsername(username);
    }
}
