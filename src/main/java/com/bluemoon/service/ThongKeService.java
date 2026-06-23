package com.bluemoon.service;

import com.bluemoon.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ThongKeService {

    private final GiaoDichRepository giaoDichRepository;
    private final HoaDonRepository hoaDonRepository;
    private final HoDanRepository hoDanRepository;
    private final NhanKhauRepository nhanKhauRepository;
    private final PhuongTienRepository phuongTienRepository;

    // 1. Logic tính Doanh thu
    public Map<String, Object> getThongKeDoanhThu() {
        Long tongDoanhThu = giaoDichRepository.tinhTongDoanhThuThangHienTai();
        return Map.of("tongDoanhThu", tongDoanhThu != null ? tongDoanhThu : 0L);
    }

    // 2. Logic tính Công nợ (Đã bọc lỗi an toàn)
    public Map<String, Object> getThongKeCongNo() {
        List<Object[]> list = hoaDonRepository.tinhCongNo();
        long soLuong = 0;
        long tongTien = 0;

        if (list != null && !list.isEmpty() && list.get(0) != null) {
            Object[] result = list.get(0);
            soLuong = result[0] != null ? ((Number) result[0]).longValue() : 0;
            tongTien = result[1] != null ? ((Number) result[1]).longValue() : 0;
        }

        return Map.of(
            "soLuongHoaDonNo", soLuong, 
            "tongTienNo", tongTien
        );
    }

    // 3. Logic thống kê Dân cư
    public Map<String, Object> getThongKeDanCu() {
        return Map.of(
            "tongHoDan", hoDanRepository.count(),
            "tongNhanKhau", nhanKhauRepository.count(),
            "phuongTien", phuongTienRepository.count()
        );
    }
}
