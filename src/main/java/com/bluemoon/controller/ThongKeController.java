package com.bluemoon.controller;

import com.bluemoon.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/thong-ke")
@RequiredArgsConstructor
// 1. ĐÃ SỬA: Khớp tên quyền với Database và cho phép cả Kế toán truy cập
@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('KE_TOAN')") 
public class ThongKeController {

    // 2. ĐÃ SỬA: Tên biến phải viết thường chữ cái đầu tiên + Tách rõ Repository
    private final GiaoDichRepository giaoDichRepository;
    private final HoaDonRepository hoaDonRepository;
    private final HoDanRepository hoDanRepository;
    private final NhanKhauRepository nhanKhauRepository;
    private final PhuongTienRepository phuongTienRepository;

    @GetMapping("/doanh-thu")
    public ResponseEntity<?> getDoanhThu() {
        Long tongDoanhThu = giaoDichRepository.tinhTongDoanhThuThangHienTai();
        // Tránh lỗi trả về Null nếu tháng này chưa thu được đồng nào
        return ResponseEntity.ok(Map.of("tongDoanhThu", tongDoanhThu != null ? tongDoanhThu : 0));
    }

    @GetMapping("/cong-no")
    public ResponseEntity<?> getCongNo() {
        List<Object[]> list = hoaDonRepository.tinhCongNo();
        long soLuong = 0;
        long tongTien = 0;

        // 3. ĐÃ SỬA: Chốt chặn an toàn chống sập Server khi không có ai nợ tiền
        if (list != null && !list.isEmpty() && list.get(0) != null) {
            Object[] result = list.get(0);
            soLuong = result[0] != null ? ((Number) result[0]).longValue() : 0;
            tongTien = result[1] != null ? ((Number) result[1]).longValue() : 0;
        }
        
        return ResponseEntity.ok(Map.of(
                "soLuongHoaDonNo", soLuong, 
                "tongTienNo", tongTien
        ));
    }

    @GetMapping("/dan-cu")
    public ResponseEntity<?> getDanCu() {
        // 4. ĐÃ SỬA: Gọi đúng các Repository có sẵn hàm count() của Spring Data JPA
        return ResponseEntity.ok(Map.of(
            "tongHoDan", hoDanRepository.count(),
            "tongNhanKhau", nhanKhauRepository.count(),
            "phuongTien", phuongTienRepository.count()
        ));
    }
}
