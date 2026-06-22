package com.bluemoon.service;

import com.bluemoon.model.ChiTietHoaDon;
import com.bluemoon.model.HoaDon;
import com.bluemoon.repository.ChiTietHoaDonRepository;
import com.bluemoon.repository.HoaDonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChiTietHoaDonService {
    
    private final ChiTietHoaDonRepository chiTietRepo;
    // Bổ sung thêm kho chứa Hóa Đơn để lấy ra cộng tiền
    private final HoaDonRepository hoaDonRepo; 

    @Transactional
    public ChiTietHoaDon addPhiVaoHoaDon(Long hoaDonId, ChiTietHoaDon chiTietMoi) {
        
        // 1. Lấy Hóa đơn gốc ra
        HoaDon hoaDon = hoaDonRepo.findById(hoaDonId)
            .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy hóa đơn ID: " + hoaDonId));

        // 2. Chặn đứng nếu hóa đơn đã được thanh toán
        if ("da_thanh_toan".equals(hoaDon.getTrangThai())) {
            throw new IllegalStateException("Hóa đơn này đã chốt thanh toán, không được phép thêm phí phát sinh!");
        }

        // 3. Gắn hóa đơn vào chi tiết và Lưu chi tiết
        chiTietMoi.setHoaDon(hoaDon);
        ChiTietHoaDon savedChiTiet = chiTietRepo.save(chiTietMoi);

        // 4. BƯỚC QUAN TRỌNG NHẤT: Cập nhật lại Tổng tiền của Hóa đơn
        long tongTienMoi = hoaDon.getTongTien() + chiTietMoi.getThanhTien();
        hoaDon.setTongTien(tongTienMoi);
        hoaDonRepo.save(hoaDon);

        return savedChiTiet;
    }
}
