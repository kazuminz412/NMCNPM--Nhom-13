package com.bluemoon.service;

import com.bluemoon.model.HoaDon;
import com.bluemoon.repository.HoaDonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HoaDonService {

    private final HoaDonRepository hoaDonRepo;

    // 1. TẠO HÓA ĐƠN ĐỊNH KỲ (Gọi Stored Procedure dưới Database)
    @Transactional
    public void taoHoaDonDinhKy(String thangNam) {
        // Chặn tạo trùng: Nếu tháng này đã có hóa đơn rồi thì báo lỗi
        if (hoaDonRepo.existsByThang(thangNam)) {
            throw new RuntimeException("Tháng " + thangNam + " đã được khởi tạo hóa đơn trước đó!");
        }
        // Gọi Stored Procedure sp_TaoHoaDonDinhKy để tự động tính tiền
        hoaDonRepo.taoHoaDonDinhKy(thangNam);
    }

    // 2. TÌM KIẾM THEO THÁNG (Nếu không truyền tháng thì lấy toàn bộ)
    public List<HoaDon> timKiemTheoThang(String thangNam) {
        if (thangNam == null || thangNam.isEmpty()) {
            return hoaDonRepo.findAll();
        }
        return hoaDonRepo.findByThang(thangNam);
    }

    // 3. TÌM THEO ID (Chi tiết 1 hóa đơn)
    public HoaDon timTheoId(Long id) {
        return hoaDonRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn có ID: " + id));
    }

    // 4. XÓA HÓA ĐƠN
    @Transactional
    public void xoaHoaDon(Long id) {
        if (!hoaDonRepo.existsById(id)) {
            throw new RuntimeException("Không tìm thấy hóa đơn để xóa!");
        }
        hoaDonRepo.deleteById(id);
    }

    // 5. LẤY DANH SÁCH HÓA ĐƠN CỦA 1 HỘ DÂN (Dành cho Góc Cư Dân)
    public List<HoaDon> findByHoDanId(Long hoDanId) {
        return hoaDonRepo.findByHoDanId(hoDanId);
    }

    // 6. LẤY HÓA ĐƠN CỦA 1 HỘ DÂN THEO THÁNG CỤ THỂ
    public List<HoaDon> timTheoHoDanVaThang(Long hoDanId, String thang) {
        if (thang == null || thang.isEmpty()) {
            // Nếu không lọc tháng thì trả về toàn bộ hóa đơn của hộ đó
            return hoaDonRepo.findByHoDanId(hoDanId);
        }
        return hoaDonRepo.findByHoDanIdAndThang(hoDanId, thang);
    }
}
