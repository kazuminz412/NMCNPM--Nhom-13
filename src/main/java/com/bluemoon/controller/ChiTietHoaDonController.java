package com.bluemoon.controller;

import com.bluemoon.model.ChiTietHoaDon;
import com.bluemoon.service.ChiTietHoaDonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chi-tiet-hoa-don")
@RequiredArgsConstructor
public class ChiTietHoaDonController {

    private final ChiTietHoaDonService service;

    // Sửa lại đường dẫn để biết đang thêm phí vào Hóa Đơn nào (VD: /api/chi-tiet-hoa-don/5)
    @PostMapping("/{hoaDonId}")
    public ResponseEntity<?> addPhi(
            @PathVariable Long hoaDonId, 
            @RequestBody ChiTietHoaDon chiTiet) {
        try {
            // Đã truyền đủ 2 tham số cho Service
            ChiTietHoaDon chiTietMoi = service.addPhiVaoHoaDon(hoaDonId, chiTiet);
            return ResponseEntity.ok(chiTietMoi);
        } catch (IllegalArgumentException | IllegalStateException e) {
            // Bắt đúng câu thông báo lỗi ở Service để trả về Mã 400 cho Frontend hiển thị popup đỏ
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Lỗi hệ thống: " + e.getMessage());
        }
    }
}
