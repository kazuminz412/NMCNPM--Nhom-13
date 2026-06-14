package com.bluemoon.controller;

import com.bluemoon.dto.ThanhToanRequest;
import com.bluemoon.model.GiaoDich;
import com.bluemoon.service.GiaoDichService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api") // Rút gọn lại để định tuyến chi tiết ở bên dưới
@RequiredArgsConstructor
public class GiaoDichController {

    private final GiaoDichService service;

    // API Thu tiền: Khớp 100% với đường dẫn của Frontend (thanhtoan.html)
    @PostMapping("/thanh-toan")
    public ResponseEntity<?> xacNhanThanhToan(@RequestBody ThanhToanRequest req) {
        try {
            // Truyền 4 tham số từ DTO vào "Cỗ máy tính tiền" của Service
            GiaoDich gd = service.xacNhanThuTien(
                req.getHoaDonId(),
                req.getSoTienKhach(),
                req.getPhuongThuc(),
                req.getGhiChu()
            );
            return ResponseEntity.ok(gd);
            
        } catch (IllegalArgumentException | IllegalStateException e) {
            // Nếu khách đưa thiếu tiền hoặc hóa đơn đã đóng rồi -> Quăng lỗi đỏ về Frontend
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            // Lỗi sập Server
            return ResponseEntity.internalServerError().body("Lỗi hệ thống: " + e.getMessage());
        }
    }

    // Sau này nhóm sẽ viết thêm API cho Cư dân xem lịch sử ở đây:
    // @GetMapping("/giao-dich/me")
    // public ResponseEntity<?> xemLichSuCuaToi() { ... }
}
