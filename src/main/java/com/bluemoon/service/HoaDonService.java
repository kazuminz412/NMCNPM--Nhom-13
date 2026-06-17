package com.bluemoon.service;

import com.bluemoon.model.HoaDon;
import com.bluemoon.model.HoDan;
import com.bluemoon.repository.HoaDonRepository;
import com.bluemoon.repository.HoDanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HoaDonService {

    private final HoaDonRepository hoaDonRepository;
    private final HoDanRepository hoDanRepository;

    // 1. TẠO HÓA ĐƠN ĐỊNH KỲ (Chạy mỗi đầu tháng)
    @Transactional
    public void taoHoaDonDinhKy(String thangNam) {
        // Kiểm tra xem tháng này đã tạo hóa đơn chưa để tránh tạo trùng lặp
        if (hoaDonRepository.existsByThang(thangNam)) {
            throw new RuntimeException("Hóa đơn cho tháng " + thangNam + " đã được khởi tạo từ trước!");
        }

        // Lấy danh sách tất cả hộ dân đang hoạt động
        List<HoDan> danhSachHoDan = hoDanRepository.findAll();
        if (danhSachHoDan.isEmpty()) {
            throw new RuntimeException("Chưa có hộ dân nào trong hệ thống để tạo hóa đơn.");
        }

        // Giả lập logic sinh hóa đơn (Trong thực tế bạn sẽ gọi thêm ChiTietHoaDonService để tính tiền gửi xe, tiền dịch vụ...)
        for (HoDan hoDan : danhSachHoDan) {
            HoaDon hoaDonMoi = new HoaDon();
            hoaDonMoi.setHoDan(hoDan);
            hoaDonMoi.setThang(thangNam);
            hoaDonMoi.setTrangThai("chua_thanh_toan");
            hoaDonMoi.setTongTien(0L); // Cần update logic tính tổng tiền từ chi tiết

            hoaDonRepository.save(hoaDonMoi);
        }
    }

    // 2. TÌM KIẾM THEO THÁNG (Cho Quản lý)
    public List<HoaDon> timKiemTheoThang(String thangNam) {
        if (thangNam == null || thangNam.trim().isEmpty()) {
            return hoaDonRepository.findAll(); // Nếu không chọn tháng thì lấy tất cả
        }
        return hoaDonRepository.findByThang(thangNam);
    }

    // 3. XEM CHI TIẾT 1 HÓA ĐƠN
    public HoaDon timTheoId(Long id) {
        return hoaDonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn với mã: " + id));
    }

    // 4. XÓA HÓA ĐƠN
    @Transactional
    public void xoaHoaDon(Long id) {
        if (!hoaDonRepository.existsById(id)) {
            throw new RuntimeException("Hóa đơn không tồn tại!");
        }
        hoaDonRepository.deleteById(id);
    }

    // =======================================================
    // CÁC HÀM PHỤC VỤ RIÊNG CHO CƯ DÂN (API /api/me/...)
    // =======================================================

    // 5. Lấy toàn bộ lịch sử hóa đơn của 1 nhà
    public List<HoaDon> findByHoDanId(Long hoDanId) {
        return hoaDonRepository.findByHoDanId(hoDanId);
    }

    // 6. Cư dân tự lọc hóa đơn nhà mình theo tháng
    public List<HoaDon> timTheoHoDanVaThang(Long hoDanId, String thang) {
        if (thang == null || thang.trim().isEmpty()) {
            return findByHoDanId(hoDanId);
        }
        return hoaDonRepository.findByHoDanIdAndThang(hoDanId, thang);
    }
}