package com.bluemoon.controller;

import com.bluemoon.model.HoaDon;
import com.bluemoon.security.JwtUtils;
import com.bluemoon.service.HoaDonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hoa-don")
@RequiredArgsConstructor
// @CrossOrigin(origins = "*") // Đã bỏ đi vì đã cấu hình CORS tập trung ở SecurityConfig
public class HoaDonController {

    private final HoaDonService hoaDonService;
    private final JwtUtils jwtUtils; // Bổ sung công cụ giải mã Token

    // 1. TẠO HÓA ĐƠN ĐỊNH KỲ (Đã sửa lại tên Authority cho khớp với Database)
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('KE_TOAN')")
    @PostMapping("/tao-dinh-ky")
    public ResponseEntity<?> taoHoaDonThang(@RequestParam String thangNam) {
        try {
            hoaDonService.taoHoaDonDinhKy(thangNam);
            return ResponseEntity.ok("Đã khởi tạo thành công hóa đơn cho tháng: " + thangNam);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi: " + e.getMessage());
        }
    }

    // 2. LẤY TẤT CẢ DANH SÁCH (Chỉ Quản lý mới được xem tất cả)
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('KE_TOAN')")
    @GetMapping
    public ResponseEntity<List<HoaDon>> getDanhSachHoaDon(@RequestParam(required = false) String thangNam) {
        List<HoaDon> danhSach = hoaDonService.timKiemTheoThang(thangNam);
        return ResponseEntity.ok(danhSach);
    }

    // 3. XEM CHI TIẾT 1 HÓA ĐƠN BẤT KỲ (Dành cho Quản lý)
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('KE_TOAN')")
    @GetMapping("/{id}")
    public ResponseEntity<HoaDon> getChiTietHoaDon(@PathVariable Long id) {
        HoaDon hoaDon = hoaDonService.timTheoId(id);
        return ResponseEntity.ok(hoaDon);
    }

    // 4. XÓA HÓA ĐƠN
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> xoaHoaDon(@PathVariable Long id) {
        hoaDonService.xoaHoaDon(id);
        return ResponseEntity.ok("Đã xóa hóa đơn thành công!");
    }

    // =======================================================
    // 5. API ĐẶC QUYỀN CHO CƯ DÂN (Chống lộ dữ liệu IDOR)
    // =======================================================
    @PreAuthorize("hasAuthority('CU_DAN')")
    @GetMapping("/me")
    public ResponseEntity<?> getHoaDonCuaToi(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam(required = false) String thang) {
        
        // Bóc mã Token để lấy hoDanId
        String token = authHeader.substring(7);
        Long hoDanId = jwtUtils.extractHoDanId(token);
        
        if (hoDanId == null) {
            return ResponseEntity.badRequest().body("Tài khoản này chưa được liên kết với Hộ dân nào!");
        }

        // Gọi Service lấy danh sách hóa đơn theo đúng nhà của họ
        // Chú ý: Backend cần đảm bảo HoaDonService có hàm timTheoHoDanVaThang(Long hoDanId, String thang)
        List<HoaDon> danhSach = hoaDonService.timTheoHoDanVaThang(hoDanId, thang);
        
        return ResponseEntity.ok(danhSach);
    }
}
