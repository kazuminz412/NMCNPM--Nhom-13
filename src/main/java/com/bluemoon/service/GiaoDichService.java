package com.bluemoon.service;

import com.bluemoon.model.GiaoDich;
import com.bluemoon.model.HoaDon;
import com.bluemoon.model.NhatKyHoatDong; 
import com.bluemoon.repository.GiaoDichRepository;
import com.bluemoon.repository.HoaDonRepository;
import com.bluemoon.repository.NhatKyHoatDongRepository; 
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class GiaoDichService {

    private final GiaoDichRepository giaoDichRepo;
    private final HoaDonRepository hoaDonRepo; 
    
    // Đã chuyển kho Nhật Ký vào BÊN TRONG Class
    private final NhatKyHoatDongRepository nhatKyRepo; 

    @Transactional
    public GiaoDich xacNhanThuTien(Long hoaDonId, Long soTienKhach, String phuongThuc, String ghiChu) {
        
        // 1. Tìm Hóa đơn cần thanh toán
        HoaDon hoaDon = hoaDonRepo.findById(hoaDonId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy hóa đơn mã: " + hoaDonId));

        // 2. Chặn đứng nếu phát hiện hóa đơn này đã thu tiền rồi
        if ("da_thanh_toan".equals(hoaDon.getTrangThai())) {
            throw new IllegalStateException("Hóa đơn này đã được thanh toán trước đó!");
        }

        // 3. (Tùy chọn) Chặn nếu số tiền khách đưa ít hơn số tiền cần thu
        if (soTienKhach < hoaDon.getTongTien()) {
            throw new IllegalArgumentException("Khách đưa thiếu tiền! Cần thu: " + hoaDon.getTongTien());
        }

        // 4. Cập nhật trạng thái Hóa Đơn -> Đã thanh toán
        hoaDon.setTrangThai("da_thanh_toan");
        hoaDonRepo.save(hoaDon);

        // 5. Tạo mới tờ Biên Lai (Giao Dịch) để lưu lịch sử
        GiaoDich giaoDich = new GiaoDich();
        giaoDich.setMaGiaoDich("BL-" + System.currentTimeMillis());
        giaoDich.setHoaDon(hoaDon);
        giaoDich.setHoDan(hoaDon.getHoDan());
        giaoDich.setSoTien(hoaDon.getTongTien());
        giaoDich.setPhuongThuc(phuongThuc);
        giaoDich.setThoiGian(LocalDateTime.now());
        giaoDich.setGhiChu(ghiChu);
        
        // Phải lưu Giao dịch trước để lấy chắc chắn kết quả
        GiaoDich savedGiaoDich = giaoDichRepo.save(giaoDich);

        // 6. GHI NHẬT KÝ HOẠT ĐỘNG (Đã lấy dữ liệu thật)
        NhatKyHoatDong log = new NhatKyHoatDong();
        
        // Tự động moi tên chủ hộ và số tiền trong hệ thống ra để ghép thành câu thông báo
        String thongBao = String.format("Hộ <b>%s</b> đã thanh toán <b>%,d đ</b>", 
                                        hoaDon.getHoDan().getTenChuHo(), 
                                        hoaDon.getTongTien());
        log.setNoiDung(thongBao);
        log.setMauSac("#1E8449"); 
        log.setThoiGian(LocalDateTime.now());
        
        nhatKyRepo.save(log);

        return savedGiaoDich;
    }
}
