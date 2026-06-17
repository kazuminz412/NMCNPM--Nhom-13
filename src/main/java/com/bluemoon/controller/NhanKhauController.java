package com.bluemoon.controller;

import com.bluemoon.model.NhanKhau;
import com.bluemoon.service.NhanKhauService;
import jakarta.validation.Valid; 
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "*") // MỞ CỬA CHO FRONTEND GỌI API
@RestController
@RequestMapping("/api/nhan-khau") 
@RequiredArgsConstructor
public class NhanKhauController {
    
    private final NhanKhauService nhanKhauService;

    // 1. Lấy danh sách
    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(nhanKhauService.findAll());
    }

    // 2. Thêm mới nhân khẩu
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody NhanKhau nhanKhau) { 
        try {
            return ResponseEntity.ok(nhanKhauService.save(nhanKhau));
        } catch (RuntimeException e) {
            // Trả về JSON lỗi để Frontend dễ bắt và hiển thị
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    // 3. Cập nhật nhân khẩu
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody NhanKhau nhanKhau) {
        try {
            return ResponseEntity.ok(nhanKhauService.update(id, nhanKhau));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    // 4. Xóa nhân khẩu
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            nhanKhauService.delete(id);
            // Đóng gói thành chuẩn JSON {"message": "..."}
            return ResponseEntity.ok(Map.of("message", "Đã xóa nhân khẩu thành công"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Không thể xóa nhân khẩu này!"));
        }
    }

}
