package com.bluemoon.service;

import com.bluemoon.model.HoaDon;
import com.bluemoon.repository.HoaDonRepository;
import com.bluemoon.repository.NhatKyHoatDongRepository;
import com.bluemoon.repository.GiaoDichRepository;
import com.bluemoon.model.NhatKyHoatDong;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HoaDonService {

    private final HoaDonRepository hoaDonRepo;
    private final NhatKyHoatDongRepository nhatKyRepo;
    private final GiaoDichRepository giaoDichRepo;
    
    private void populateNgayThanhToan(List<HoaDon> list) {
        if (list != null) {
            list.forEach(hd -> {
                if ("da_thanh_toan".equals(hd.getTrangThai()) || "mot_phan".equals(hd.getTrangThai())) {
                    giaoDichRepo.findTopByHoaDonIdOrderByThoiGianDesc(hd.getId()).ifPresent(gd -> {
                        hd.setNgayThanhToan(gd.getThoiGian().toLocalDate().toString());
                    });
                }
            });
        }
    }

    // 1. TẠO HÓA ĐƠN ĐỊNH KỲ (Gọi Stored Procedure dưới Database)
    @Transactional
    public void taoHoaDonDinhKy(String thangNam) {
        // Chặn tạo trùng: Nếu tháng này đã có hóa đơn rồi thì báo lỗi
        if (hoaDonRepo.existsByThang(thangNam)) {
            throw new RuntimeException("Tháng " + thangNam + " đã được khởi tạo hóa đơn trước đó!");
        }
        // Gọi Stored Procedure sp_TaoHoaDonDinhKy để tự động tính tiền
        hoaDonRepo.taoHoaDonDinhKy(thangNam);
        
        NhatKyHoatDong log = new NhatKyHoatDong();
        log.setNoiDung(String.format("Quản trị viên đã tạo hóa đơn tháng <b>%s</b>", thangNam));
        log.setMauSac("#D35400"); 
        log.setThoiGian(LocalDateTime.now());
        nhatKyRepo.save(log);
    }

    public List<HoaDon> timKiemTheoThang(String thangNam) {
        List<HoaDon> list;
        if (thangNam == null || thangNam.isEmpty()) {
            list = hoaDonRepo.findAll();
        } else {
            list = hoaDonRepo.findByThang(thangNam);
        }
        populateNgayThanhToan(list);
        return list;
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

    // 5. Lấy danh sách hóa đơn của 1 hộ dân cụ thể (Dành cho Cư Dân)
    public List<HoaDon> findByHoDanId(Long hoDanId) {
        List<HoaDon> list = hoaDonRepo.findByHoDanId(hoDanId);
        populateNgayThanhToan(list);
        return list;
    }

    // 6. Lấy hóa đơn của 1 hộ dân trong 1 tháng cụ thể
    public List<HoaDon> timTheoHoDanVaThang(Long hoDanId, String thangNam) {
        List<HoaDon> list;
        if (thangNam == null || thangNam.isEmpty()) {
            list = hoaDonRepo.findByHoDanId(hoDanId);
        } else {
            list = hoaDonRepo.findByHoDanIdAndThang(hoDanId, thangNam);
        }
        populateNgayThanhToan(list);
        return list;
    }
}
