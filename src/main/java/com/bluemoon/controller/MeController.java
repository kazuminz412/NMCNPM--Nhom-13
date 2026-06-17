package com.bluemoon.controller;

import com.bluemoon.repository.HoDanRepository;
import com.bluemoon.repository.NhanKhauRepository;
import com.bluemoon.repository.PhuongTienRepository;
import com.bluemoon.security.JwtUtils;
import com.bluemoon.service.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/me")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('CU_DAN')") // Khóa chặt: Chỉ Cư Dân mới được gọi API này
public class MeController {

    private final JwtUtils jwtUtils;
    private final HoDanService hoDanService; // Bổ sung Service này
    private final HoaDonService hoaDonService;
    private final PhuongTienService phuongTienService;
    private final NhanKhauService nhanKhauService;
    private final HoDanRepository hoDanRepository;
    private final NhanKhauRepository nhanKhauRepository;
    private final PhuongTienRepository phuongTienRepository;

    // Hàm nội bộ: Rút trích an toàn hoDanId từ Token
    private Long getHoDanId(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            Long id = jwtUtils.extractHoDanId(jwt);
            if (id != null) return id;
        }
        throw new IllegalArgumentException("Truy cập bị từ chối: Không tìm thấy mã Hộ dân hợp lệ!");
    }

    // 1. LẤY THÔNG TIN GỐC CỦA HỘ DÂN
    @GetMapping("/ho-dan")
    public ResponseEntity<?> getThongTinNhaToi(HttpServletRequest request) {
        return ResponseEntity.ok(hoDanRepository.findById(getHoDanId(request)));
    }

    // 2. LẤY DANH SÁCH NHÂN KHẨU
    @GetMapping("/nhan-khau")
    public ResponseEntity<?> getNhanKhauCuaToi(HttpServletRequest request) {
        return ResponseEntity.ok(nhanKhauRepository.findByHoDanId(getHoDanId(request)));
    }

    // 3. LẤY DANH SÁCH PHƯƠNG TIỆN
    @GetMapping("/phuong-tien")
    public ResponseEntity<?> getPhuongTienCuaToi(HttpServletRequest request) {
        return ResponseEntity.ok(phuongTienRepository.findByHoDanId(getHoDanId(request)));
    }

    // 4. LẤY LỊCH SỬ HÓA ĐƠN (Hỗ trợ lọc theo tháng)
    @GetMapping("/hoa-don")
    public ResponseEntity<?> getHoaDonCuaToi(
            HttpServletRequest request,
            @RequestParam(required = false) String thang) {
        
        Long hoDanId = getHoDanId(request);
        
        // Nếu Frontend gửi tháng lên thì lọc theo tháng, nếu không thì lấy toàn bộ
        if (thang != null && !thang.isEmpty()) {
            return ResponseEntity.ok(hoaDonService.timTheoHoDanVaThang(hoDanId, thang));
        }
        return ResponseEntity.ok(hoaDonService.findByHoDanId(hoDanId));
    }
}
