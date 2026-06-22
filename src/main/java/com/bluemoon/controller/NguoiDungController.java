package com.bluemoon.controller;

import com.bluemoon.dto.LoginRequest;
import com.bluemoon.model.NguoiDung;
import com.bluemoon.service.NguoiDungService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/nguoi-dung")
public class NguoiDungController {

    @Autowired
    private NguoiDungService nguoiDungService;

    // ============================================
    // 0. API ĐĂNG NHẬP (Đã được phục hồi)
    // ============================================
    @PostMapping("/login")
   public ResponseEntity<?> login(@RequestBody LoginRequest request) {
    NguoiDung user = nguoiDungService.checkLogin(request.getUsername(), request.getPassword());
        if (user != null) {
            Map<String, Object> response = new HashMap<>();
            response.put("token", "fake-jwt-token-cho-sprint1"); 
            response.put("user", user);
            return ResponseEntity.ok(response); 
        }
        
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("message", "Sai tên đăng nhập hoặc mật khẩu!");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    // ============================================
    // CÁC API QUẢN LÝ TÀI KHOẢN (CRUD)
    // ============================================

    // 1. Lấy danh sách tất cả tài khoản
    @GetMapping
    public List<NguoiDung> getAllNguoiDung() {
        return nguoiDungService.findAll();
    }

    // 2. Thêm mới tài khoản (Đã bỏ hoDanId đi vì đây là tài khoản, không phải nhân khẩu)
    @PostMapping
    public ResponseEntity<?> createNguoiDung(@RequestBody NguoiDung nguoiDung) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(nguoiDungService.save(nguoiDung));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 3. Cập nhật thông tin tài khoản
    @PutMapping("/{id}")
    public ResponseEntity<NguoiDung> updateNguoiDung(@PathVariable Long id, @RequestBody NguoiDung nguoiDungDetails) {
        return ResponseEntity.ok(nguoiDungService.update(id, nguoiDungDetails));
    }

    // 4. Xóa tài khoản
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNguoiDung(@PathVariable Long id) {
        nguoiDungService.delete(id);
        return ResponseEntity.ok("Đã xóa tài khoản thành công!");
    }
}
